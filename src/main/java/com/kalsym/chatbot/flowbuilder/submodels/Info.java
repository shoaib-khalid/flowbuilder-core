package com.kalsym.chatbot.flowbuilder.submodels;

import com.kalsym.chatbot.flowbuilder.models.enums.VertexType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class Info {

    private String title;
    private String text;
    private VertexType type;
}
