package com.kalsym.chatbot.flowbuilder.submodels;

import lombok.Getter;
import lombok.Setter;
import com.kalsym.chatbot.flowbuilder.models.enums.ConditionOperator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class Condition {

    private ConditionOperator operator;
    private List<ConditionGroup> groups;
    private Step step;

  
}
