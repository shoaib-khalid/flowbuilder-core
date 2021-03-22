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

import com.kalsym.chatbot.flowbuilder.repositories.MxgraphtriggerRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphconnectionstartRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphconnectionendRepositories;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mxgraphtrigger {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject triggers;
  public String mxId;
  public String flowId;
  
  public Mxgraphtrigger() {}
  
  public static void UpdateTrigger(String flowId, String actionType, JsonObject jsonObject, 
          MxgraphtriggerRepositories mxTriggerRepositories, 
          MxgraphconnectionstartRepositories mxgraphconnectionStartRepositories,
          MxgraphconnectionendRepositories mxgraphconnectionEndRepositories) {
        if (actionType.equalsIgnoreCase("add")) {
            LOG.info("Adding new trigger into flowId:"+flowId);
            Object dataObject = BasicDBObject.parse(jsonObject.toString());
            Mxgraphtrigger trigger = new Mxgraphtrigger();
            trigger.flowId = flowId;
            String mxId = jsonObject.get("@id").getAsString();
            trigger.mxId = mxId;
            trigger.triggers = (DBObject)dataObject;
            mxTriggerRepositories.save(trigger);
            LOG.info("New trigger added flowId:"+flowId+" mxId:"+mxId+" id:"+trigger.id);
        } else if (actionType.equalsIgnoreCase("update")) {
            LOG.info("update trigger into flowId:"+flowId);            
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphtrigger trigger = mxTriggerRepositories.findByMxId(flowId, mxId);
            if (trigger!=null) {
                Object dataObject = BasicDBObject.parse(jsonObject.toString());
                trigger.triggers = (DBObject)dataObject;
                mxTriggerRepositories.save(trigger);
                LOG.info("trigger updated flowId:"+flowId+" mxId:"+mxId+" id:"+trigger.id);
            } else {
                LOG.error("update trigger not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        } else if (actionType.equalsIgnoreCase("delete")) {
            LOG.info("delete trigger into flowId:"+flowId);
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphtrigger trigger = mxTriggerRepositories.findByMxId(flowId, mxId);
            if (trigger!=null) {
                mxTriggerRepositories.delete(trigger);
                LOG.info("trigger deleted flowId:"+flowId+" mxId:"+mxId+" id:"+trigger.id);
                //find connection start & connection end relate to this trigger
                List MxgraphconnectionStartList = mxgraphconnectionStartRepositories.findByUserObjectMxId(flowId, mxId);
                for (int i=0;i<MxgraphconnectionStartList.size();i++) {
                    Mxgraphconnectionstart connectionStart = (Mxgraphconnectionstart)MxgraphconnectionStartList.get(i);
                    mxgraphconnectionStartRepositories.delete(connectionStart);
                    LOG.info("connectionStart deleted flowId:"+flowId+" triggerId:"+mxId+" id:"+connectionStart.id);
                }
                List MxgraphconnectionEndList = mxgraphconnectionEndRepositories.findByUserObjectMxId(flowId, mxId);
                for (int i=0;i<MxgraphconnectionEndList.size();i++) {
                    Mxgraphconnectionend connectionEnd = (Mxgraphconnectionend)MxgraphconnectionEndList.get(i);
                    mxgraphconnectionEndRepositories.delete(connectionEnd);
                    LOG.info("connectionEnd deleted flowId:"+flowId+" triggerId:"+mxId+" id:"+connectionEnd.id);
                }
            } else {
                LOG.error("delete trigger not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        }
  }
}
