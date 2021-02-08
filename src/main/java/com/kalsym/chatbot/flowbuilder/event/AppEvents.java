/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.kalsym.chatbot.flowbuilder.auth.Authorities;
import com.kalsym.chatbot.flowbuilder.auth.AuthService;
import com.kalsym.chatbot.flowbuilder.auth.AuthResult;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

@Component
public class AppEvents {

    @Autowired 
    private AuthService authService;
    
    private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
    @EventListener(ApplicationReadyEvent.class)
    public void startApp() {

        LOG.info("Executing Startup Task");
        
        //send login first to get token
        AuthResult loginResult = authService.SendLoginRequest();
        if (!loginResult.isSuccess) {
            LOG.info("Login Failed");
        } else {
            LOG.info("Login Success");
            String token = loginResult.returnString;
            LOG.info("Token : "+token);
            //send request to authentication service for list of api
            ArrayList<Authorities> authrList = new ArrayList<Authorities>();

            //conversation  
            Authorities authr1 = new Authorities();
            authr1.serviceId="flow_builder";                        
            authr1.description="{GET /conversation/}";
            authr1.name="getConversation";
            authr1.id="flow-builder_conversations-get";
            authrList.add(authr1);

            Authorities authr2 = new Authorities();
            authr2.serviceId="flow_builder";  
            authr2.description="{POST /conversation/}";
            authr2.name="createConversation";
            authr2.id="flow-builder_conversations-post";
            authrList.add(authr2);

            Authorities authr3 = new Authorities();
            authr3.serviceId="flow_builder";  
            authr3.description="{DELETE /conversation/{id}}";
            authr3.name="deleteConversation";
            authr3.id="flow-builder_conversations-delete-by-id";
            authrList.add(authr3);

            Authorities authr4 = new Authorities();
            authr4.serviceId="flow_builder";          
            authr4.description="{PATCH /conversation/{id}}";
            authr4.name="updateConversation";
            authr4.id="flow-builder_conversations-patch-by-id";
            authrList.add(authr4);        

            //flow
            Authorities authr5 = new Authorities();
            authr5.serviceId="flow_builder";                          
            authr5.description="{GET /flow}";
            authr5.name="getFlow";
            authr5.id="flow-builder_flows-get";
            authrList.add(authr5);

            Authorities authr6 = new Authorities();
            authr6.serviceId="flow_builder";                  
            authr6.description="{POST /flow}";
            authr6.name="createFlow";
            authr6.id="flow-builder_flows-post";
            authrList.add(authr6);

            Authorities authr7 = new Authorities();
            authr7.serviceId="flow_builder";
            authr7.description="{DELETE /flow/{id}}";
            authr7.name="deleteFlow";
            authr7.id="flow-builder_flows-delete-by-id";
            authrList.add(authr7);

            Authorities authr8 = new Authorities();
            authr8.serviceId="flow_builder";
            authr8.description="{PATCH /flow/{id}}";
            authr8.name="updateFlow";
            authr8.id="flow-builder_flows-patch-by-id";
            authrList.add(authr8);

            Authorities authr9 = new Authorities();
            authr9.serviceId="flow_builder";        
            authr9.description="{GET /flow/{id}/{botId}/{topVertexId}}";
            authr9.name="getFlow";
            authr9.id="flow-builder_flows-get-by-id-botid-topvertexid";
            authrList.add(authr9);

            //mxgraph

            Authorities authr10 = new Authorities();
            authr10.serviceId="flow_builder";        
            authr10.description="{PATCH /mxgraph/{action-type}/{flow-id}}";
            authr10.name="autoSaveMx";
            authr10.id="flow-builder_mxgraph-patch-by-flowid";
            authrList.add(authr10);

            Authorities authr11 = new Authorities();
            authr11.serviceId="flow_builder";        
            authr11.description="{GET /mxgraph/{flow-id}}";
            authr11.name="getMxObject";
            authr11.id="flow-builder_mxgraph-get-by-flowid";
            authrList.add(authr11);

            Authorities authr12 = new Authorities();
            authr12.serviceId="flow_builder";
            authr12.description="{POST /mxgraph/{flow-id}}";
            authr12.name="createAndUpdateMx";
            authr12.id="flow-builder_mxgraph-post-by-flowid";
            authrList.add(authr12);

            Authorities authr13 = new Authorities();
            authr13.serviceId="flow_builder";
            authr13.description="{DELETE /mxgraph/{flow-id}}";
            authr13.name="deleteMx";
            authr13.id="flow-builder_mxgraph-delete-by-flowid";
            authrList.add(authr13);

            Authorities authr14 = new Authorities();
            authr14.serviceId="flow_builder";
            authr14.description="{GET /mxgraph/publish/{flow-id}}";
            authr14.name="getPublishMxObject";
            authr14.id="flow-builder_mxgraph-get-publish-by-flowid";
            authrList.add(authr14);

            //vertex

            Authorities authr15 = new Authorities();
            authr15.serviceId="flow_builder";        
            authr15.description="{GET /vertex/}";
            authr15.name="getVertex";
            authr15.id="flow-builder_vertex-get";
            authrList.add(authr15);

            Authorities authr16 = new Authorities();
            authr16.serviceId="flow_builder";
            authr16.description="{POST /vertex/}";
            authr16.name="createVertex";
            authr16.id="flow-builder_vertex-post";
            authrList.add(authr16);

            Authorities authr17 = new Authorities();
            authr17.serviceId="flow_builder";
            authr17.description="{DELETE /vertex/{id}}";
            authr17.name="deleteVertex";
            authr17.id="flow-builder_vertex-delete-by-id";
            authrList.add(authr17);

            Authorities authr18 = new Authorities();
            authr18.serviceId="flow_builder";
            authr18.description="{PATCH /vertex/{id}}";
            authr18.name="updateVertex";
            authr18.id="flow-builder_vertex-patch-by-id";
            authrList.add(authr18);

            LOG.info("Send AuthList:"+authrList.size()+" to Auth-Service");
            authService.SendRequest(authrList,token);
        }
    }
}