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

public class Publishmxcell {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
  
  @Id
  public String id;

  public DBObject mxCell;
  public String mxId;
  public String flowId;
  
  public Publishmxcell() {}
 
  
}
