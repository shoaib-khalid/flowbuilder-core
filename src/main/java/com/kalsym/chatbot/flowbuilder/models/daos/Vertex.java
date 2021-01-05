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

import com.kalsym.chatbot.flowbuilder.submodels.Option;
import com.kalsym.chatbot.flowbuilder.submodels.Info;
import com.kalsym.chatbot.flowbuilder.submodels.Step;
import com.kalsym.chatbot.flowbuilder.submodels.Action;

import java.util.List;


public class Vertex {

  @Id
  public String id;

  public Info info;
  public List<Option> options;
  public Step step;
  public String flowId;
  public String customVariableName;
  public String dataVariable;
  public List<Action> actions;
  public DBObject validation;
  public String mxId;
  public int isTopVertex=0;
  
  public Vertex() {
  }

  public Vertex(String flowId, String customVariableName) {
    this.flowId = flowId;
    this.customVariableName = customVariableName;
  }

  
}
