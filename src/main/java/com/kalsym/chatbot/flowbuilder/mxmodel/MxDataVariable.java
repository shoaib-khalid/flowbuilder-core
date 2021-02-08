package com.kalsym.chatbot.flowbuilder.mxmodel;

import com.kalsym.chatbot.flowbuilder.submodels.*;
import com.kalsym.chatbot.flowbuilder.models.enums.VertexType;
import lombok.Getter;
import lombok.Setter;

import com.google.gson.*;
import java.util.List;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class MxDataVariable {

    private VertexType type;
    
    private String vertexId;
    
    private List<DataVariable> dataList;
    
    private List<Button> buttonList;
}
