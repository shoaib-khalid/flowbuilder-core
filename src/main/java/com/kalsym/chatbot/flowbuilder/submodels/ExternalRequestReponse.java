package com.kalsym.chatbot.flowbuilder.submodels;

import com.kalsym.chatbot.flowbuilder.models.enums.DataFomat;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class ExternalRequestReponse {

    DataFomat format;

    List<ExternalRequestResponseMapping> mapping;
}
