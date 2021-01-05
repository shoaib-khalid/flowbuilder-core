package com.kalsym.chatbot.flowbuilder.submodels;

import com.kalsym.chatbot.flowbuilder.models.enums.VertexActionType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class Action {

    private VertexActionType type;
    
    private ExternalRequest externalRequest;
}
