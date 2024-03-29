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
public class HttpsConnection {
    
    private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
    public static HttpResult SendHttpsRequest(String refId, String targetUrl, HashMap httpHeader, String requestBody, int connectTimeout, int waitTimeout) {
        HttpResult response = new HttpResult();
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }};

            // Ignore differences between given hostname and certificate hostname
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new SecureRandom());

            LOG.info("[" + refId + "] Sending Request to :" +targetUrl);
            URL url = new URL(targetUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setSSLSocketFactory(sc.getSocketFactory());
            con.setHostnameVerifier(hv);
            con.setConnectTimeout(connectTimeout);
            con.setReadTimeout(waitTimeout);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            
            //Set HTTP Headers
            LOG.info("[" + refId + "] Set HTTP Header");
            Iterator it = httpHeader.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                LOG.info("[" + refId + "] "+(String)pair.getKey() + " = " + (String)pair.getValue());
                con.setRequestProperty((String)pair.getKey(), (String)pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            
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