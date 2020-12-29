/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.models.daos;

/**
 *
 * @author user
 */
import org.springframework.data.annotation.Id;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;
import org.bson.types.ObjectId;

import org.json.JSONObject;
import org.json.JSONArray;

public class Vertex {

  @Id
  public String id;

  public DBObject info;
  public DBObject[] options;
  public DBObject step;
  public ObjectId flowId;
  public String customVariableName;
  public DBObject[] actions;
  public DBObject validation;

  public Vertex() {}

  public Vertex(ObjectId flowId, String customVariableName) {
    this.flowId = flowId;
    this.customVariableName = customVariableName;
  }

  @Override
  public String toString() {
    return String.format(
        "Flow[id=%s, title='%s', description='%s']",
        id, customVariableName, customVariableName);
  }
  
}
