/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.models;

import com.kalsym.chatbot.flowbuilder.submodels.Data;
import com.mongodb.DBObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author user
 */
@Getter
@Setter
@ToString
public class ConversationPayload {
    
  public Data data;
  public String senderId;
  public String referenceId;
  public String flowId;

}
