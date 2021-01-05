package com.kalsym.chatbot.flowbuilder.submodels;

import com.kalsym.chatbot.flowbuilder.models.enums.MatchOperator;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class ConditionGroup {

    private String field;
    private MatchOperator match;
    private Boolean caseSensitive;
    private String value;

   
}
