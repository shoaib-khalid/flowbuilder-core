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

import com.kalsym.chatbot.flowbuilder.repositories.MxgraphconnectionendRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mxgraphconnectionend {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject connectionends;
  public String mxId;
  public String flowId;
  
  public Mxgraphconnectionend() {}
  
  public static void UpdateConnectionEnd(String flowId, String actionType, JsonObject jsonObject, MxgraphconnectionendRepositories mxgraphconnectionendRepositories) {
        if (actionType.equalsIgnoreCase("add")) {
            LOG.info("Adding new ConnectionEnd into flowId:"+flowId);
            Object dataObject = BasicDBObject.parse(jsonObject.toString());
            Mxgraphconnectionend connectionend = new Mxgraphconnectionend();
            connectionend.flowId = flowId;
            String mxId = jsonObject.get("@id").getAsString();
            connectionend.mxId = mxId;
            connectionend.connectionends = (DBObject)dataObject;
            mxgraphconnectionendRepositories.save(connectionend);
            LOG.info("New ConnectionEnd added flowId:"+flowId+" mxId:"+mxId+" id:"+connectionend.id);
        } else if (actionType.equalsIgnoreCase("update")) {
            LOG.info("update ConnectionEnd into flowId:"+flowId);            
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphconnectionend connectionend = mxgraphconnectionendRepositories.findByMxId(flowId, mxId);
            if (connectionend!=null) {
                Object dataObject = BasicDBObject.parse(jsonObject.toString());
                connectionend.connectionends = (DBObject)dataObject;
                mxgraphconnectionendRepositories.save(connectionend);
                LOG.info("ConnectionEnd updated flowId:"+flowId+" mxId:"+mxId+" id:"+connectionend.id);
            } else {
                LOG.error("update ConnectionEnd not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        } else if (actionType.equalsIgnoreCase("delete")) {
            LOG.info("delete ConnectionEnd into flowId:"+flowId);
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphconnectionend connectionend = mxgraphconnectionendRepositories.findByMxId(flowId, mxId);
            if (connectionend!=null) {
                mxgraphconnectionendRepositories.delete(connectionend);
                LOG.info("ConnectionEnd deleted flowId:"+flowId+" mxId:"+mxId+" id:"+connectionend.id);
            } else {
                LOG.error("delete ConnectionEnd not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        }
  }
}
