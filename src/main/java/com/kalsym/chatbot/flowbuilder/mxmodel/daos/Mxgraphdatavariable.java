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

import com.kalsym.chatbot.flowbuilder.repositories.MxgraphdatavariableRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.*;
import com.kalsym.chatbot.flowbuilder.submodels.DataVariable;

public class Mxgraphdatavariable {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject dataVariable;
  public String flowId;
  public String vertexId;
  
  public Mxgraphdatavariable() {}
 
  public static void UpdateDataVariable(String flowId, String vertexId, String actionType, JsonObject jsonObject, MxgraphdatavariableRepositories mxDataVariableRepositories) {
        if (actionType.equalsIgnoreCase("add") || actionType.equalsIgnoreCase("update")) {
            LOG.info("Adding new datavariable into flowId:"+flowId+" vertexId:"+vertexId);
            Mxgraphdatavariable dataVariableObjectExisting = mxDataVariableRepositories.getMxByFlowIdAndVertexId(flowId, vertexId);
            if (dataVariableObjectExisting==null) {
                Object dataObject = BasicDBObject.parse(jsonObject.toString());
                Mxgraphdatavariable dataVariableObject = new Mxgraphdatavariable();
                dataVariableObject.flowId = flowId;
                dataVariableObject.vertexId = vertexId;
                dataVariableObject.dataVariable = (DBObject)dataObject;
                mxDataVariableRepositories.save(dataVariableObject);
                LOG.info("New dataVariable added flowId:"+flowId+" vertexId:"+dataVariableObject.vertexId);
            } else {
                Object dataObject = BasicDBObject.parse(jsonObject.toString());
                dataVariableObjectExisting.dataVariable = (DBObject)dataObject;
                mxDataVariableRepositories.save(dataVariableObjectExisting);
                LOG.info("New dataVariable updated flowId:"+flowId+" vertexId:"+dataVariableObjectExisting.vertexId);
            }
            
        /*} else if (actionType.equalsIgnoreCase("update")) {
            LOG.info("update datavariable into flowId:"+flowId+" vertexId:"+vertexId);            
            Object dataObject = BasicDBObject.parse(jsonObject.toString());
            Mxgraphdatavariable dataVariableObject = mxDataVariableRepositories.getMxByFlowIdAndVertexId(flowId, vertexId);
            if (dataVariableObject!=null) {
                dataVariableObject.dataVariable = (DBObject)dataObject;
                mxDataVariableRepositories.save(dataVariableObject);
                LOG.info("dataVariable updated flowId:"+flowId+" vertexId:"+dataVariableObject.vertexId);
            } else {
                LOG.error("update dataVariable not exist for flowId:"+flowId+" vertexId:"+vertexId);
            }
*/
        } else if (actionType.equalsIgnoreCase("delete")) {
            LOG.info("delete datavariable into flowId:"+flowId+" vertexId:"+vertexId);            
            Mxgraphdatavariable dataVariableObject = mxDataVariableRepositories.getMxByFlowIdAndVertexId(flowId, vertexId);
            if (dataVariableObject!=null) {
                mxDataVariableRepositories.delete(dataVariableObject);
                LOG.info("dataVariable deleted flowId:"+flowId+" vertexId:"+dataVariableObject.vertexId);
            } else {
                LOG.info("delete dataVariable not exist for flowId:"+flowId+" vertexId:"+dataVariableObject.vertexId);
            }
        }
  }
}
