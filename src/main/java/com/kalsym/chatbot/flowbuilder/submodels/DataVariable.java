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
public class DataVariable {

    private String id;
    private String dataVariable;
    private String path;
    private String optional;
    
   
    @Override
    public String toString() {
        return "Id="+this.id+" dataVariable="+this.dataVariable+" path="+path+" optional="+optional;
    }
}
