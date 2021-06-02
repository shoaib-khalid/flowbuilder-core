package com.kalsym.chatbot.flowbuilder.controllers;

import java.util.List;
import java.util.Optional;

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
import com.kalsym.chatbot.flowbuilder.models.daos.Flowdraft;
import com.kalsym.chatbot.flowbuilder.models.FlowPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.kalsym.chatbot.flowbuilder.repositories.FlowsRepositories;
import org.springframework.security.access.prepost.PreAuthorize;

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

    //list
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", name = "flow-get-by-id")
    @PreAuthorize("hasAnyAuthority('flow-get-by-id', 'all')")
    public ResponseEntity<ProcessResult> getFlow(
            @RequestHeader("Authorization") String auth,
            @PathVariable(required = false) String id,
            @PathVariable(required = false) String botId,
            @PathVariable(required = false) String topVertexId
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("-------------------getFlow. Check token validity:" + auth);

            if (id != null) {
                Optional<Flow> flowList = chatflowsRepositories.findById(id);
                if (flowList.isPresent()) {
                    response.setData(flowList.get());
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                }
            } else if (botId != null && topVertexId != null) {
                List<Flow> flowList = chatflowsRepositories.getByBotIdsAndTopVertexId(botId, topVertexId);
                if (!flowList.isEmpty()) {
                    response.setData(flowList);
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                }
            } else if (botId != null) {
                List<Flow> flowList = chatflowsRepositories.getByBotIds(botId);
                if (!flowList.isEmpty()) {
                    response.setData(flowList);
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                }
            } else {
                List<Flow> flowList = chatflowsRepositories.findAll();
                if (!flowList.isEmpty()) {
                    response.setData(flowList);
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                }
            }

            LOG.info("[" + auth + "] getFlow Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] getFlow Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //create
    @RequestMapping(method = RequestMethod.POST, value = "", name = "flow-post")
    @PreAuthorize("hasAnyAuthority('flow-post', 'all')")
    public ResponseEntity<ProcessResult> createFlow(
            @RequestHeader("Authorization") String auth,
            @RequestBody(required = true) FlowPayload flowPayLoad
    ) {

        ProcessResult response = new ProcessResult();

        try {
            // This returns a JSON or XML with the users
            LOG.info("[" + auth + "] -------------------createFlow. Check token validity:" + auth);

            LOG.info("[" + auth + "] createFlow. FlowPayload :" + flowPayLoad.toString());
            String title = flowPayLoad.getTitle();
            String description = flowPayLoad.getDescription();
            String[] botIds = flowPayLoad.getBotIds();
            String topVertexId = flowPayLoad.getTopVertexId();
            Flow chatFlow = new Flow();
            chatFlow.title = title;
            chatFlow.description = description;
            chatFlow.botIds = botIds;
            chatFlow.topVertexId = topVertexId;
            chatFlow.status = "draft";
            chatFlow.ownerId = flowPayLoad.getOwnerId();
            chatFlow.storeId = flowPayLoad.getStoreId();
            chatflowsRepositories.save(chatFlow);

            LOG.info("[" + auth + "] createFlow Finish");
            response.setData(chatFlow);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] createFlow Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", name = "flow-patch-by-id")
    @PreAuthorize("hasAnyAuthority('flow-patch-by-id', 'all')")
    public ResponseEntity<ProcessResult> updateFlow(
            @RequestHeader("Authorization") String auth,
            @PathVariable("id") String id,
            @RequestBody(required = true) FlowPayload flowPayLoad
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("[" + auth + "] -------------------updateFlow. Check token validity:" + auth);
            LOG.info("[" + auth + "] updateFlow. Flow Id:"+id+" FlowPayload :" + flowPayLoad.toString());
            String title = flowPayLoad.getTitle();
            String description = flowPayLoad.getDescription();
            String[] botIds = flowPayLoad.getBotIds();
            String topVertexId = flowPayLoad.getTopVertexId();
            Optional<Flow> flowList = chatflowsRepositories.findById(id);
            if (flowList.isPresent()) {
                Flow chatFlow = flowList.get();
                chatFlow.id = id;
                chatFlow.title = title;
                chatFlow.description = description;
                if (botIds!=null) {
                    chatFlow.botIds = botIds;
                }
                if (topVertexId!=null) {
                    chatFlow.topVertexId = topVertexId;
                }
                chatflowsRepositories.save(chatFlow);
                
                LOG.info("[" + auth + "] updateFlow Finish");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
        } catch (Exception exp) {
            LOG.error("[" + auth + "] updateFlow Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //delete
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", name = "flow-delete-by-id")
    @PreAuthorize("hasAnyAuthority('flow-delete-by-id', 'all')")
    public ResponseEntity<ProcessResult> deleteFlow(
            @RequestHeader("Authorization") String auth,
            @PathVariable("id") String id
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("-------------------deleteFlow. Check token validity:" + auth);
            chatflowsRepositories.deleteById(id);
            LOG.info("[" + auth + "] deleteFlow Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] deleteFlow Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    //list
    @RequestMapping(method = RequestMethod.GET, value = {"/getall/{ownerId}"}, name = "flow-get-by-ownerId")
    @PreAuthorize("hasAnyAuthority('flow-get-by-ownerId', 'all')")
    public ResponseEntity<ProcessResult> getAllFlowByOwnerId(
            @RequestHeader("Authorization") String auth,
            @PathVariable(required = true) String ownerId
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("-------------------getAllFlowByOwnerId. Check token validity:" + auth);

            List<Flow> flowList = chatflowsRepositories.getByOwnerId(ownerId);
            if (!flowList.isEmpty()) {
                response.setData(flowList);
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
           
            LOG.info("[" + auth + "] getAllFlowByOwnerId Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] getAllFlowByOwnerId Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
