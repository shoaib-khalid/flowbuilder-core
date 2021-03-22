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

import java.util.List;

public class Flow {

  @Id
  public String id;

  public String title;
  public String[] botIds;
  public String topVertexId;
  public String description;
  public String status;
  public String ownerId;
  //public DBObject data;

  public Flow() {}

}
