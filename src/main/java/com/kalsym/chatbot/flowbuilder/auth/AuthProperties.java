/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.chatbot.flowbuilder.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    @Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    public String loginUrl;
    public String syncUrl;
    public Integer connectTimeout;
    public Integer waitTimeout;
    public String moduleUsername;
    public String modulePassword;
    
    public AuthProperties() {
        
    }
}
