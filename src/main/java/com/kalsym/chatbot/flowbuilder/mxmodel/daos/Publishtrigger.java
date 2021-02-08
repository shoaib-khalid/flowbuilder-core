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

import com.kalsym.chatbot.flowbuilder.repositories.MxgraphtriggerRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publishtrigger {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject triggers;
  public String mxId;
  public String flowId;
  
  public Publishtrigger() {}
  
 
}
