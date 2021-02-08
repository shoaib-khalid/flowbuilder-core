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


import com.kalsym.chatbot.flowbuilder.repositories.MxgraphuserobjectRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphmxcellRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphtriggerRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.*;

import java.util.List;

public class Publishuserobject {
  
  private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
  @Id
  public String id;

  public DBObject userObject;
  public String mxId;
  public String flowId;
  
  public Publishuserobject() {}
  
}
