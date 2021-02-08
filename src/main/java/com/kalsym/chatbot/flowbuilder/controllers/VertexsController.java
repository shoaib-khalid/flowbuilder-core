package com.kalsym.chatbot.flowbuilder.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kalsym.chatbot.flowbuilder.ProcessResult;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import com.kalsym.chatbot.flowbuilder.models.VertexPayload;
import com.kalsym.chatbot.flowbuilder.submodels.Option;
import com.kalsym.chatbot.flowbuilder.submodels.Info;
import com.kalsym.chatbot.flowbuilder.submodels.Step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;
import org.bson.types.ObjectId;

import com.kalsym.chatbot.flowbuilder.repositories.VertexsRepositories;


/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/vertex")
public class VertexsController {
   
    private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
    @Autowired 
    private VertexsRepositories vertexRepositories;
    
   @RequestMapping(method= RequestMethod.GET, value="/")
	public ResponseEntity<ProcessResult> getVertex(
                @RequestHeader("Authorization") String auth
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("getVertex. Check token validity:"+auth); 
                    
                    List<Vertex> vertexList = vertexRepositories.findAll();
                    if (!vertexList.isEmpty()) {
                        response.setData(vertexList);
                        response.setStatus(HttpStatus.OK.value());
                    }

                    LOG.info("["+auth+"] getVertex Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] getVertex Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
    @RequestMapping(method= RequestMethod.POST, value="/")
	public ResponseEntity<ProcessResult> createVertex(
                @RequestHeader("Authorization") String auth,
                @RequestBody(required = true) VertexPayload vertexPayLoad
                ) {
		
                ProcessResult response = new ProcessResult();
           
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("createVertex. Check token validity:"+auth); 
                    
                    String flowId = vertexPayLoad.getFlowId();
                    String customVariableName = vertexPayLoad.getCustomVariableName();
                    List<Option> options = vertexPayLoad.getOptions();
                    Info info = vertexPayLoad.getInfo();
                    Step step = vertexPayLoad.getStep();
                    
                    Vertex vertex = new Vertex();
                    vertex.flowId=flowId;
                    vertex.customVariableName=customVariableName;
                    
                    /*Object infoObject = BasicDBObject.parse(info.toString());
                    vertex.info = (DBObject)infoObject;
                    
                    Object stepObject = BasicDBObject.parse(step.toString());
                    vertex.step = (DBObject)stepObject;
                    
                    DBObject[] optionArray = new DBObject[options.length()];
                    for (int i=0;i<options.length();i++) {
                        JSONObject opt = (JSONObject)options.get(i);
                        Object optionObject = BasicDBObject.parse(opt.toString());
                        optionArray[i]=(DBObject)optionObject;
                    }
                    vertex.options = optionArray;
                    */
                    
                    vertex.info = info;
                    vertex.step = step;
                    vertex.options = options;
                    vertexRepositories.save(vertex);
                   
                    LOG.info("["+auth+"] createVertex Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] createFlow Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
        
    @RequestMapping(method= RequestMethod.DELETE, value="/{id}")
	public ResponseEntity<ProcessResult> deleteVertex(
                @RequestHeader("Authorization") String auth,
                @PathVariable("id") String id
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("deleteFlow. Check token validity:"+auth);                     
                    vertexRepositories.deleteById(id);                   
                    LOG.info("["+auth+"] deleteFlow Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] deleteFlow Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
    @RequestMapping(method= RequestMethod.PATCH, value="/{id}")
	public ResponseEntity<ProcessResult> updateVertex(
                @RequestHeader("Authorization") String auth,
                @PathVariable("id") String id,
                @RequestBody(required = true) VertexPayload vertexPayLoad
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("updateVertex. Check token validity:"+auth);    
                    
                    /*JSONObject jsonObj = new JSONObject(jsonRequest);
                    ObjectId flowId = new ObjectId(jsonObj.getString("flowId"));
                    String customVariableName = jsonObj.getString("customVariableName");                    
                    JSONArray options = jsonObj.getJSONArray("options");
                    JSONObject info = jsonObj.getJSONObject("info");
                    JSONObject step = jsonObj.getJSONObject("step");
                    */
                    
                    String flowId = vertexPayLoad.getFlowId();
                    String customVariableName = vertexPayLoad.getCustomVariableName();
                    List<Option> options = vertexPayLoad.getOptions();
                    Info info = vertexPayLoad.getInfo();
                    Step step = vertexPayLoad.getStep();
                    
                    Vertex vertex = new Vertex();
                    vertex.id = id;
                    vertex.flowId=flowId;
                    vertex.customVariableName=customVariableName;
                    vertex.info = info;
                    vertex.step = step;
                    vertex.options = options;
                    
                    /*
                    Object infoObject = BasicDBObject.parse(info.toString());
                    vertex.info = (DBObject)infoObject;
                    
                    Object stepObject = BasicDBObject.parse(step.toString());
                    vertex.step = (DBObject)stepObject;
                    
                    DBObject[] optionArray = new DBObject[options.length()];
                    for (int i=0;i<options.length();i++) {
                        JSONObject opt = (JSONObject)options.get(i);
                        Object optionObject = BasicDBObject.parse(opt.toString());
                        optionArray[i]=(DBObject)optionObject;
                    }
                    vertex.options = optionArray;
                    */
                    
                    vertexRepositories.save(vertex);
                    
                    LOG.info("["+auth+"] updateVertex finish"); 
                    
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] updateVertex Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
}
