package com.kalsym.chatbot.flowbuilder.submodels;

import com.kalsym.chatbot.flowbuilder.models.enums.ValidationInputType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class Validation {

    private ValidationInputType inputType;
    private String phone;
    private String regex;
    private Retry retry;

}
