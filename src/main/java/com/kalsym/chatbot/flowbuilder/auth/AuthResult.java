/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.chatbot.flowbuilder.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author user
 */
    @Getter
    @Setter
    @ToString
public class AuthResult {
    public String returnCode;
    public boolean isSuccess;
    public String returnString;
    
    public AuthResult() {
        
    }
}
