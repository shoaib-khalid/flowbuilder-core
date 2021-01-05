package com.kalsym.chatbot.flowbuilder.controllers;

import java.util.List;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kalsym.chatbot.flowbuilder.ProcessResult;
import com.kalsym.chatbot.flowbuilder.models.daos.Flow;
import com.kalsym.chatbot.flowbuilder.models.FlowPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.kalsym.chatbot.flowbuilder.repositories.FlowsRepositories;


/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/flow")
public class FlowsController {
   
    private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
    @Autowired 
    private FlowsRepositories chatflowsRepositories;
    
   @RequestMapping(method= RequestMethod.GET, value="/")
	public ResponseEntity<ProcessResult> getFlow(
                @RequestHeader("Authorization") String auth
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("getFlow. Check token validity:"+auth); 
                    
                    List<Flow> flowList = chatflowsRepositories.findAll();
                    if (!flowList.isEmpty()) {
                        response.setData(flowList);
                        response.setStatus(HttpStatus.OK.value());
                    }

                    LOG.info("["+auth+"] getFlow Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] getFlow Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
    @RequestMapping(method= RequestMethod.POST, value="/")
	public ResponseEntity<ProcessResult> createFlow(
                @RequestHeader("Authorization") String auth,
                @RequestBody(required = true) FlowPayload flowPayLoad
                ) {
		
                ProcessResult response = new ProcessResult();
           
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("["+auth+"] createFlow. Check token validity:"+auth);
                    
                    LOG.info("["+auth+"] createFlow. FlowPayload :"+flowPayLoad.toString());                    
                    String title = flowPayLoad.getTitle();
                    String description = flowPayLoad.getDescription();
                    String botId = flowPayLoad.getBotId();
                    String topVertexId = flowPayLoad.getTopVertexId();
                    Flow chatFlow = new Flow();
                    chatFlow.title=title;
                    chatFlow.description=description;
                    chatFlow.botId=botId;
                    chatFlow.topVertexId=topVertexId;
                    chatflowsRepositories.save(chatFlow);
                   
                    LOG.info("["+auth+"] createFlow Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] createFlow Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
        
    @RequestMapping(method= RequestMethod.DELETE, value="/{id}")
	public ResponseEntity<ProcessResult> deleteFlow(
                @RequestHeader("Authorization") String auth,
                @PathVariable("id") String id
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("deleteFlow. Check token validity:"+auth);                     
                    chatflowsRepositories.deleteById(id);                   
                    LOG.info("["+auth+"] deleteFlow Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] deleteFlow Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
    @RequestMapping(method= RequestMethod.PATCH, value="/{id}")
	public ResponseEntity<ProcessResult> updateFlow(
                @RequestHeader("Authorization") String auth,
                @PathVariable("id") String id,
                @RequestBody(required = true) FlowPayload flowPayLoad
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("["+auth+"] updateFlow. Check token validity:"+auth);                     
                    LOG.info("["+auth+"] updateFlow. FlowPayload :"+flowPayLoad.toString());                    
                    String title = flowPayLoad.getTitle();
                    String description = flowPayLoad.getDescription();
                    String botId = flowPayLoad.getBotId();
                    String topVertexId = flowPayLoad.getTopVertexId();
                    Flow chatFlow = new Flow();
                    chatFlow.id = id;
                    chatFlow.title=title;
                    chatFlow.description=description;
                    chatFlow.botId=botId;
                    chatFlow.topVertexId=topVertexId;
                    chatflowsRepositories.save(chatFlow);
                    
                    LOG.info("["+auth+"] updateFlow Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] updateFlow Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
}
