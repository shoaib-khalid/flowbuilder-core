/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.models;

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
public class FlowPayload {
    
    public String title;
    public String botId;
    public String topVertexId;
    public String description;
    //public DBObject data;

}
