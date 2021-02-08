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


import com.kalsym.chatbot.flowbuilder.repositories.MxgraphuserobjectRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphmxcellRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphtriggerRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.*;

import java.util.List;

public class Mxgraphuserobject {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
  @Id
  public String id;

  public DBObject userObject;
  public String mxId;
  public String flowId;
  
  public Mxgraphuserobject() {}
  
  public static void UpdateUserObject(String flowId, String actionType, JsonObject jsonObject, 
          MxgraphuserobjectRepositories mxUserObjectRepositories, 
          MxgraphmxcellRepositories mxCellRepositories, 
          MxgraphtriggerRepositories mxTriggerRepositories) {
      
        if (actionType.equalsIgnoreCase("add")) {
            LOG.info("Adding new userObject into flowId:"+flowId);
            Object dataObject = BasicDBObject.parse(jsonObject.toString());
            Mxgraphuserobject userObject = new Mxgraphuserobject();
            userObject.flowId = flowId;
            String mxId = jsonObject.get("@id").getAsString();
            userObject.mxId = mxId;
            userObject.userObject = (DBObject)dataObject;
            mxUserObjectRepositories.save(userObject);
            LOG.info("New userObject added flowId:"+flowId+" mxId:"+mxId+" id:"+userObject.id);
        } else if (actionType.equalsIgnoreCase("update")) {
            LOG.info("update userObject into flowId:"+flowId);            
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphuserobject userObject = mxUserObjectRepositories.findByMxId(flowId, mxId);
            if (userObject!=null) {
                Object dataObject = BasicDBObject.parse(jsonObject.toString());
                userObject.userObject = (DBObject)dataObject;
                mxUserObjectRepositories.save(userObject);
                LOG.info("userObject updated flowId:"+flowId+" mxId:"+mxId+" id:"+userObject.id);
            } else {
                LOG.error("update userObject not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        } else if (actionType.equalsIgnoreCase("delete")) {
            LOG.info("delete userObject into flowId:"+flowId);
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphuserobject userObject = mxUserObjectRepositories.findByMxId(flowId, mxId);
            if (userObject!=null) {
                mxUserObjectRepositories.delete(userObject);
                LOG.info("userObject deleted flowId:"+flowId+" mxId:"+mxId+" id:"+userObject.id);
            } else {
                LOG.error("delete userObject not exist for flowId:"+flowId+" mxId:"+mxId);
            }
            //also delete mxcell
            List<Mxgraphmxcell> mxcellList = mxCellRepositories.findByUserObjectMxId(flowId, mxId);
            for (int i=0;i<mxcellList.size();i++) {
                Mxgraphmxcell mxcell = mxcellList.get(i);
                mxCellRepositories.delete(mxcell);
                LOG.info("mxcell deleted flowId:"+flowId+" mxId:"+mxId+" id:"+mxcell.id);
            }
            //also delete trigger
            List<Mxgraphtrigger> triggerList = mxTriggerRepositories.findByUserObjectMxId(flowId, mxId);
            for (int i=0;i<triggerList.size();i++) {
                Mxgraphtrigger trigger = triggerList.get(i);
                mxTriggerRepositories.delete(trigger);
                LOG.info("trigger deleted flowId:"+flowId+" mxId:"+mxId+" id:"+trigger.id);
            }
        }
  }
}
