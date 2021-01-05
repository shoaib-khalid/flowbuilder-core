package com.kalsym.chatbot.flowbuilder.submodels;

import com.kalsym.chatbot.flowbuilder.models.enums.VertexTargetType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class Step {

    private VertexTargetType targetType;
    private String targetId;
    private String sourceId;
    
    @Override
    public String toString() {
        return "targetType="+this.targetType+" targetId="+this.targetId+" sourceId="+this.sourceId;
    }
}
