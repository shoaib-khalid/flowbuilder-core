package com.kalsym.chatbot.flowbuilder.submodels;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class Retry {

    private int count;
    private String message;

    private Step failureStep;
}
