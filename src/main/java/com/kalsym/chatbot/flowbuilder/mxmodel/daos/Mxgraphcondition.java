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

import com.kalsym.chatbot.flowbuilder.repositories.MxgraphconditionRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mxgraphcondition {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject conditions;
  public String mxId;
  public String flowId;
  
  public Mxgraphcondition() {}
  
  public static void UpdateCondition(String flowId, String actionType, JsonObject jsonObject, MxgraphconditionRepositories mxConditionRepositories) {
        if (actionType.equalsIgnoreCase("add")) {
            LOG.info("Adding new condition into flowId:"+flowId);
            Object dataObject = BasicDBObject.parse(jsonObject.toString());
            Mxgraphcondition condition = new Mxgraphcondition();
            condition.flowId = flowId;
            String mxId = jsonObject.get("@id").getAsString();
            condition.mxId = mxId;
            condition.conditions = (DBObject)dataObject;
            mxConditionRepositories.save(condition);
            LOG.info("New condition added flowId:"+flowId+" mxId:"+mxId+" id:"+condition.id);
        } else if (actionType.equalsIgnoreCase("update")) {
            LOG.info("update condition into flowId:"+flowId);            
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphcondition condition = mxConditionRepositories.findByMxId(flowId, mxId);
            if (condition!=null) {
                Object dataObject = BasicDBObject.parse(jsonObject.toString());
                condition.conditions = (DBObject)dataObject;
                mxConditionRepositories.save(condition);
                LOG.info("condition updated flowId:"+flowId+" mxId:"+mxId+" id:"+condition.id);
            } else {
                LOG.error("update condition not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        } else if (actionType.equalsIgnoreCase("delete")) {
            LOG.info("delete condition into flowId:"+flowId);
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphcondition condition = mxConditionRepositories.findByMxId(flowId, mxId);
            if (condition!=null) {
                mxConditionRepositories.delete(condition);
                LOG.info("condition deleted flowId:"+flowId+" mxId:"+mxId+" id:"+condition.id);
            } else {
                LOG.error("delete condition not exist for flowId:"+flowId+" mxId:"+mxId);
            }
        }
  }
}
