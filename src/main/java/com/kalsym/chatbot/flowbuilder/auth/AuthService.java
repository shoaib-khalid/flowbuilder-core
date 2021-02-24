/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.chatbot.flowbuilder.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import sun.misc.BASE64Encoder;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author user
 */

@Service
public class  AuthService {
    
    private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
    private final String loginUrl;
    private final String syncUrl;    
    private final Integer connectTimeout;
    private final Integer waitTimeout;
    private final String moduleUsername;
    private final String modulePassword;
      
    @Autowired
    public AuthService(AuthProperties prop) {
        
        this.loginUrl = prop.getLoginUrl();
        this.syncUrl = prop.getSyncUrl();
        this.connectTimeout = prop.getConnectTimeout();
        this.waitTimeout = prop.getWaitTimeout();  
        this.moduleUsername = prop.getModuleUsername();
        this.modulePassword = prop.getModulePassword();
    }
    
    public AuthResult SendLoginRequest() {
        AuthResult result = new AuthResult();
        
        
        // Use this builder to construct a Gson instance when you need to set configuration options other than the default.
	GsonBuilder gsonBuilder = new GsonBuilder();

        // This is the main class for using Gson. Gson is typically used by first constructing a Gson instance and then invoking toJson(Object) or fromJson(String, Class) methods on it. 
        // Gson instances are Thread-safe so you can reuse them freely across multiple threads.
        Gson gson = gsonBuilder.create();
        
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("username", moduleUsername);
        jsonRequest.addProperty("password", modulePassword);
        
        String JSONObject = jsonRequest.toString();
        LOG.info("\nConverted JSONObject ==> " + JSONObject);
                
//        HttpResult httpResult = HttpConnection.SendHttpRequest("SYNC-AUTHORITIES", this.loginUrl, "POST", getLoginHttpHeader(), JSONObject, this.connectTimeout, this.waitTimeout);
//        LOG.info("HTTP Result httpResponseCode:"+httpResult.httpResponseCode+" resultCode:"+httpResult.resultCode+" resultString:"+httpResult.resultString);
//        
//        result.isSuccess=false;
//        if (httpResult.httpResponseCode==200 || httpResult.httpResponseCode==202) {
//            //extract token
//            JsonObject resp = new Gson().fromJson(httpResult.resultString, JsonObject.class);
//            result.returnString = resp.get("data").getAsJsonObject().get("session").getAsJsonObject().get("id").getAsString();            
//            result.isSuccess=true;
//        }
        return result;

    }
    
    public AuthResult SendRequest(List<Authorities> athourityList, String token) {
        AuthResult result = new AuthResult();
        
        HashMap httpHeader = getHttpHeader(token);
        
        // Use this builder to construct a Gson instance when you need to set configuration options other than the default.
	GsonBuilder gsonBuilder = new GsonBuilder();

        // This is the main class for using Gson. Gson is typically used by first constructing a Gson instance and then invoking toJson(Object) or fromJson(String, Class) methods on it. 
        // Gson instances are Thread-safe so you can reuse them freely across multiple threads.
        Gson gson = gsonBuilder.create();

        String JSONObject = gson.toJson(athourityList);
        LOG.info("\nConverted JSONObject ==> " + JSONObject);
                
        HttpResult httpResult = HttpConnection.SendHttpRequest("SYNC-AUTHORITIES", this.syncUrl, "PUT", httpHeader, JSONObject, this.connectTimeout, this.waitTimeout);
        LOG.info("HTTP Result httpResponseCode:"+httpResult.httpResponseCode+" resultCode:"+httpResult.resultCode+" resultString:"+httpResult.resultString);
        return result;

    }
    
    private static HashMap getHttpHeader(String token) {
        HashMap <String,String> headers = new HashMap<String, String>();
        headers.put("Authorization","Bearer "+token);
        headers.put("Content-Type","application/json");
        return headers;
    }
    
    
    private static HashMap getLoginHttpHeader() {
        HashMap <String,String> headers = new HashMap<String, String>();
        headers.put("Content-Type","application/json");
        return headers;
    }
   
}
