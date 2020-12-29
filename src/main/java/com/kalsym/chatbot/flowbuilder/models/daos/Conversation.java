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

import org.json.JSONObject;
import org.json.JSONArray;

public class Conversation {

  @Id
  public String id;
 
  public DBObject data;
  public String createdDate;
  public String lastModifiedDate;
  public String senderId;
  public String referenceId;
  public String flowId;

  public Conversation() {}

  public Conversation(String flowId, String referenceId) {
    this.flowId = flowId;
    this.referenceId = referenceId;
  }

  @Override
  public String toString() {
    return String.format(
        "Coonversation[flowId=%s, referenceId='%s', senderId='%s']",
        flowId, referenceId, senderId);
  }
  
}
