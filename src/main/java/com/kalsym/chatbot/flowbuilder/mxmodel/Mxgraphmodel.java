/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.mxmodel;

/**
 *
 * @author user
 */
import org.springframework.data.annotation.Id;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;

import org.json.JSONObject;
import org.json.JSONArray;

public class Mxgraphmodel {

  @Id
  public String id;

  public DBObject mxGraphModel;
  public String flowId;
  
  public Mxgraphmodel() {}

}
