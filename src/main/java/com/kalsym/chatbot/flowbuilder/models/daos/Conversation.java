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

import com.kalsym.chatbot.flowbuilder.submodels.Data;

public class Conversation {

  @Id
  public String id;
 
  public Data data;
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

}
