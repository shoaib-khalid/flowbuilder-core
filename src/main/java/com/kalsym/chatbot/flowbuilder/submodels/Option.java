package com.kalsym.chatbot.flowbuilder.submodels;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter
public class Option {

    private String text;
    private Step step;
    private String value;
    private String id;
    
    public boolean match(String value, HashMap<String, String> dataVariables) {

        if (this.value.startsWith("$%") && this.value.endsWith("$%")) {
            this.value = this.value.replace("$%", "");
            this.value = dataVariables.get(this.value);
        }

        return this.value.equalsIgnoreCase(value);

    }
    
    @Override
    public String toString() {
        return "Id="+this.id+" Text="+this.text+" Value="+value+" Step="+step;
    }
}
