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

import com.kalsym.chatbot.flowbuilder.submodels.Option;
import com.kalsym.chatbot.flowbuilder.submodels.Info;
import com.kalsym.chatbot.flowbuilder.submodels.Step;
import com.kalsym.chatbot.flowbuilder.submodels.Action;
import com.kalsym.chatbot.flowbuilder.submodels.DataVariable;
import com.kalsym.chatbot.flowbuilder.submodels.Handover;
import com.kalsym.chatbot.flowbuilder.submodels.Condition;

import java.util.List;
import lombok.ToString;


@ToString
public class Vertex {

  @Id
  public String id;

  public Info info;
  public List<Option> options;
  public Step step;
  public String flowId;
  public String customVariableName;
  //public List<DataVariable> dataVariables;
  public String dataVariable;
  public List<Action> actions;
  public DBObject validation;
  public String mxId;
  public int isTopVertex=0;
  public int isLastVertex=0;
  public Handover handover;
  public List<Condition> conditions;
  
  public Vertex() {
  }

  public Vertex(String flowId, String customVariableName) {
    this.flowId = flowId;
    this.customVariableName = customVariableName;
  }
}
