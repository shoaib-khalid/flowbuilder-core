package com.kalsym.chatbot.flowbuilder.submodels;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
@ToString
public class ExternalRequest {

    private String url;
    private HttpMethod httpMethod;
    private HashMap<String, String>[] headers;
    private ExternalRequestBody body;

    private ExternalRequestReponse response;

    private Step errorStep;

}
