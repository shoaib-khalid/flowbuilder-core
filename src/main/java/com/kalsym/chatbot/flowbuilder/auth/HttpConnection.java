/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.chatbot.flowbuilder.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author user
 */
public class HttpConnection {
    
    private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
    public static HttpResult SendHttpRequest(String refId, String targetUrl, String httpMethod, HashMap httpHeader, String requestBody, int connectTimeout, int waitTimeout) {
        HttpResult response = new HttpResult();
        try {
           
            LOG.info("[" + refId + "] Sending Request to :" +targetUrl);
            URL url = new URL(targetUrl);
            sun.net.www.protocol.http.HttpURLConnection con = (sun.net.www.protocol.http.HttpURLConnection) url.openConnection();
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(waitTimeout);
            con.setRequestMethod(httpMethod);
            
            
            //Set HTTP Headers
            if (httpHeader!=null) {
                LOG.info("[" + refId + "] Set HTTP Header");
                Iterator it = httpHeader.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    LOG.info("[" + refId + "] "+(String)pair.getKey() + " = " + (String)pair.getValue());
                    con.setRequestProperty((String)pair.getKey(), (String)pair.getValue());
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
            
            con.setDoOutput(true);
            con.connect();

            //for post paramters in JSON Format
            OutputStream os = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            
            LOG.info("[" + refId + "] Request Body :" + requestBody);
            osw.write(requestBody);
            osw.flush();
            osw.close();

            int responseCode = con.getResponseCode();
            LOG.info("[" + refId + "] HTTP Response code:" + responseCode);
            BufferedReader in;
            if (responseCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuilder httpMsgResp = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                httpMsgResp.append(inputLine);
            }
            in.close();

            LOG.info("[" + refId + "] Response of topup :" + httpMsgResp.toString());
            
            response.httpResponseCode = responseCode;
            response.resultCode = 0;
            response.resultString = httpMsgResp.toString();
            
        } catch (SocketTimeoutException ex) {
            if (ex.getMessage().equals("Read timed out")){
                response.resultCode = -2;
                response.resultString = ex.getMessage();
                LOG.error("[" + refId + "] Exception : " + ex.getMessage());
            }
            else{
                response.resultCode = -1;
                response.resultString = ex.getMessage();
                LOG.error("[" + refId + "] Exception : " + ex.getMessage());
            }
        } catch (Exception ex) {
            //exception occur
            response.resultCode = -1;
            response.resultString = ex.getMessage();
            LOG.info("[" + refId + "] Exception during send request : ", ex);        
        }
        
        return response;
    }
}