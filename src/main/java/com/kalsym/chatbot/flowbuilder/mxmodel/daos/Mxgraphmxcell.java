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

import com.kalsym.chatbot.flowbuilder.repositories.MxgraphmxcellRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.*;

public class Mxgraphmxcell {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject mxCell;
  public String mxId;
  public String flowId;
  
  public Mxgraphmxcell() {}
 
  public static void UpdateMxcell(String flowId, String actionType, JsonObject jsonObject, MxgraphmxcellRepositories mxCellRepositories) {
        if (actionType.equalsIgnoreCase("add")) {
            LOG.info("Adding new mxcell into flowId:"+flowId);
            Object dataObject = BasicDBObject.parse(jsonObject.toString());
            Mxgraphmxcell mxcell = new Mxgraphmxcell();
            mxcell.flowId = flowId;
            String mxId = jsonObject.get("@id").getAsString();
            mxcell.mxId = mxId;
            mxcell.mxCell = (DBObject)dataObject;
            mxCellRepositories.save(mxcell);
            LOG.info("New mxcell added flowId:"+flowId+" mxId:"+mxId+" id:"+mxcell.id);
        } else if (actionType.equalsIgnoreCase("update")) {
            LOG.info("update mxcell into flowId:"+flowId);            
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphmxcell mxcell = mxCellRepositories.findByMxId(flowId, mxId);
            if (mxcell!=null) {
                Object dataObject = BasicDBObject.parse(jsonObject.toString());
                mxcell.mxCell = (DBObject)dataObject;
                mxCellRepositories.save(mxcell);
                LOG.info("mxcell updated flowId:"+flowId+" mxId:"+mxId+" id:"+mxcell.id);
            } else {
                LOG.error("update mxcell not exist for flowId:"+flowId+" mxId:"+mxId+" id:"+mxcell.id);
            }
        } else if (actionType.equalsIgnoreCase("delete")) {
            LOG.info("deleted mxcell into flowId:"+flowId);
            String mxId = jsonObject.get("@id").getAsString();
            Mxgraphmxcell mxcell = mxCellRepositories.findByMxId(flowId, mxId);
            if (mxcell!=null) {
                mxCellRepositories.delete(mxcell);
                LOG.info("mxcell deleted flowId:"+flowId+" mxId:"+mxId+" id:"+mxcell.id);
            } else {
                LOG.error("delete mxcell not exist for flowId:"+flowId+" mxId:"+mxId+" id:"+mxcell.id);
            }
        }
  }
}
