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
import com.kalsym.chatbot.flowbuilder.models.ConversationPayload;
import com.kalsym.chatbot.flowbuilder.models.daos.Conversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.kalsym.chatbot.flowbuilder.repositories.ConversationsRepositories;
import com.kalsym.chatbot.flowbuilder.utils.DateTimeUtil;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/conversation")
public class ConversationsController {

    private static final Logger LOG = LoggerFactory.getLogger("Launcher");

    @Autowired
    private ConversationsRepositories conversationsRepositories;

    @RequestMapping(method = RequestMethod.GET, value = "/", name = "conversations-get")
    @PreAuthorize("hasAnyAuthority('conversations-get', 'all')")
    public ResponseEntity<ProcessResult> getConversation(
            @RequestHeader("Authorization") String auth
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("getConversation. Check token validity:" + auth);

            List<Conversation> conversationList = conversationsRepositories.findAll();
            if (!conversationList.isEmpty()) {
                response.setData(conversationList);
                response.setStatus(HttpStatus.OK.value());
            }

            LOG.info("[" + auth + "] getConversation Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] getConversation Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/", name = "conversations-post")
    @PreAuthorize("hasAnyAuthority('conversations-post', 'all')")
    public ResponseEntity<ProcessResult> createConversation(
            @RequestHeader("Authorization") String auth,
            @RequestBody(required = true) ConversationPayload conversationPayload
    ) {

        ProcessResult response = new ProcessResult();

        try {
            // This returns a JSON or XML with the users
            LOG.info("createConversation. Check token validity:" + auth);

            /*JSONObject jsonObj = new JSONObject(jsonRequest);
                    JSONObject data = jsonObj.getJSONObject("data");
                    String senderId = jsonObj.getString("senderId");
                    String referenceId = jsonObj.getString("referenceId");
                    String flowId = jsonObj.getString("flowId");
                    
                    Conversation conversation = new Conversation();
                    
                    Object dataObject = BasicDBObject.parse(data.toString());
                    conversation.data = (DBObject)dataObject;
                    conversation.createdDate = DateTimeUtil.currentTimestamp();
                    conversation.lastModifiedDate = DateTimeUtil.currentTimestamp(); 
                    conversation.senderId = senderId;
                    conversation.referenceId = referenceId;
                    conversation.flowId = flowId;
             */
            Conversation conversation = new Conversation();

            conversation.data = conversationPayload.getData();
            conversation.createdDate = DateTimeUtil.currentTimestamp();
            conversation.lastModifiedDate = DateTimeUtil.currentTimestamp();
            conversation.senderId = conversationPayload.getSenderId();
            conversation.referenceId = conversationPayload.getReferenceId();
            conversation.flowId = conversationPayload.getFlowId();

            conversationsRepositories.save(conversation);

            LOG.info("[" + auth + "] createConversation Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] createConversation Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", name = "conversations-delete")
    @PreAuthorize("hasAnyAuthority('conversations-delete', 'all')")
    public ResponseEntity<ProcessResult> deleteConversation(
            @RequestHeader("Authorization") String auth,
            @PathVariable("id") String id
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("deleteConversation. Check token validity:" + auth);
            conversationsRepositories.deleteById(id);
            LOG.info("[" + auth + "] deleteConversation Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] deleteConversation Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", name = "conversations-patch")
    @PreAuthorize("hasAnyAuthority('conversations-patch', 'all')")
    public ResponseEntity<ProcessResult> updateConversation(
            @RequestHeader("Authorization") String auth,
            @PathVariable("id") String id,
            @RequestBody(required = true) ConversationPayload conversationPayload
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("updateConversation. Check token validity:" + auth);

            Conversation conversation = new Conversation();

            conversation.id = id;
            conversation.data = conversationPayload.getData();
            conversation.createdDate = DateTimeUtil.currentTimestamp();
            conversation.lastModifiedDate = DateTimeUtil.currentTimestamp();
            conversation.senderId = conversationPayload.getSenderId();
            conversation.referenceId = conversationPayload.getReferenceId();
            conversation.flowId = conversationPayload.getFlowId();

            conversationsRepositories.save(conversation);

            LOG.info("[" + auth + "] updateConversation Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] updateConversation Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
