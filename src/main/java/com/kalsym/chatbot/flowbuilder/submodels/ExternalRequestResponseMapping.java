package com.kalsym.chatbot.flowbuilder.submodels;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class ExternalRequestResponseMapping {

    private String dataVariable;
    private String path;
    private boolean optional;
}
