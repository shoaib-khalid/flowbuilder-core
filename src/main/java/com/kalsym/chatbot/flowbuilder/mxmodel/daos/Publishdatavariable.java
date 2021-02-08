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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.*;
import com.kalsym.chatbot.flowbuilder.submodels.DataVariable;

public class Publishdatavariable {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject dataVariable;
  public String flowId;
  public String vertexId;
  
  public Publishdatavariable() {}
 
  
}
