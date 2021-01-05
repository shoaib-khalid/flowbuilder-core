package com.kalsym.chatbot.flowbuilder.submodels;

import com.kalsym.chatbot.flowbuilder.models.enums.DataFomat;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class ExternalRequestBody {

    DataFomat format;

    String payload;
}
