/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.mxmodel;

import com.kalsym.chatbot.flowbuilder.submodels.*;
import com.kalsym.chatbot.flowbuilder.models.enums.VertexActionType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Sarosh
 */
@Getter
@Setter

public class MxObject {
    private MxObjectType objectType;
    private Object object;
    
     public MxObject(MxObjectType ot, Object o ) {
         this.objectType=ot;
         this.object=o;
     }
}
