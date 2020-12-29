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

public class Flow {

  @Id
  public String id;

  public String title;
  public String botId;
  public String topVertexId;
  public String description;
  public DBObject data;

  public Flow() {}

  public Flow(String title, String description) {
    this.title = title;
    this.description = description;
  }

  @Override
  public String toString() {
    return String.format(
        "Flow[id=%s, title='%s', description='%s']",
        id, title, description);
  }
  
}
