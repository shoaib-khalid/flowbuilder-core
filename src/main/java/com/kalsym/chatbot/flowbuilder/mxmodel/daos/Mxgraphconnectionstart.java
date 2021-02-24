/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.mxmodel.daos;

/**
 *
 * @author user
 */
import org.springframework.data.annotation.Id;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;
import com.google.gson.*;

import com.kalsym.chatbot.flowbuilder.repositories.MxgraphconnectionstartRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mxgraphconnectionstart {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject connectionstarts;
  public String mxId;
  public String flowId;
  
  public Mxgraphconnectionstart() {}
  
  public static void UpdateConnectionStart(String flowId, String actionType, JsonObject jsonObject, MxgraphconnectionstartRepositories mxgraphconnectionstartRepositories) {
        if (actionType.equalsIgnoreCase("add")) {
            LOG.info("Adding new ConnectionStart into flowId:"+flowId);
            Object dataObject = BasicDBObject.parse(jsonObject.toString());
            Mxgraphconnectionstart connectionstart = new Mxgraphconnectionstart();
            connectionstart.flowId = flowId;
            String mxId = jsonObject.get("@id").getAsString();
            connectionstart.mxId = mxId;
            connectionstart.connectionstarts = (DBObject)dataObject;
            mxgraphconnectionstartRepositories.save(connectionstart);
            LOG.info("New ConnectionStart added flowId:"+flowId+" mxId:"+mxId+" id:"+connectionstart.id);
        } else if (actionType.equalsIgnoreCase("update")) {
            LOG.info("update ConnectionStart into flowId:"+flowId);            
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphconnectionstart connectionstart = mxgraphconnectionstartRepositories.findByMxId(flowId, mxId);
            if (connectionstart!=null) {
                Object dataObject = BasicDBObject.parse(jsonObject.toString());
                connectionstart.connectionstarts = (DBObject)dataObject;
                mxgraphconnectionstartRepositories.save(connectionstart);
                LOG.info("ConnectionStart updated flowId:"+flowId+" mxId:"+mxId+" id:"+connectionstart.id);
            } else {
                LOG.error("update ConnectionStart not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        } else if (actionType.equalsIgnoreCase("delete")) {
            LOG.info("delete ConnectionStart into flowId:"+flowId);
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphconnectionstart connectionstart = mxgraphconnectionstartRepositories.findByMxId(flowId, mxId);
            if (connectionstart!=null) {
                mxgraphconnectionstartRepositories.delete(connectionstart);
                LOG.info("ConnectionStart deleted flowId:"+flowId+" mxId:"+mxId+" id:"+connectionstart.id);
            } else {
                LOG.error("delete ConnectionStart not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        }
  }
}
