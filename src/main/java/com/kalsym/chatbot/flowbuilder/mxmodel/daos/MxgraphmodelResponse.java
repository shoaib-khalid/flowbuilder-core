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

public class MxgraphmodelResponse {

  @Id
  public String id;

  public Object mxGraphModel;
  //public Object root;
  public String flowId;
  public Object data;
  
  public MxgraphmodelResponse() {}

}
