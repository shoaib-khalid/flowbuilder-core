package com.kalsym.chatbot.flowbuilder.mxmodel;

import com.kalsym.chatbot.flowbuilder.submodels.*;
import com.kalsym.chatbot.flowbuilder.models.enums.VertexActionType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class Edge {

    private String id;
    
    private String parent;
    
    private String source;
    
    private String target;
    
    private String value;
}
