/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.models;

import com.mongodb.DBObject;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import com.kalsym.chatbot.flowbuilder.submodels.Option;
import com.kalsym.chatbot.flowbuilder.submodels.Info;
import com.kalsym.chatbot.flowbuilder.submodels.Step;

/**
 *
 * @author user
 */
@Getter
@Setter
@ToString
public class VertexPayload {
    
    public String flowId;
    public String customVariableName;
    public List<Option> options;
    public String description;
    public Info info;
    public Step step;
    
}
