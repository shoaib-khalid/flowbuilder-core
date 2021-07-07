package com.kalsym.chatbot.flowbuilder.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Iterator;
import com.google.gson.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kalsym.chatbot.flowbuilder.ProcessResult;
import com.kalsym.chatbot.flowbuilder.models.HttpReponse;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.MxgraphmodelResponse;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import com.kalsym.chatbot.flowbuilder.models.daos.Flow;
import com.kalsym.chatbot.flowbuilder.submodels.Option;
import com.kalsym.chatbot.flowbuilder.submodels.Step;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.*;
import com.kalsym.chatbot.flowbuilder.repositories.ConversationsRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kalsym.chatbot.flowbuilder.repositories.VertexsRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.FlowsRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphuserobjectRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphmxcellRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphtriggerRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphdatavariableRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphconditionRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphconnectionstartRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphconnectionendRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.PublishuserobjectRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.PublishmxcellRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.PublishtriggerRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.PublishdatavariableRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.PublishconditionRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.PublishconnectionstartRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.PublishconnectionendRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.PublishconditionRepositories;

import com.kalsym.chatbot.flowbuilder.utils.VertexDecoder;
import com.kalsym.chatbot.flowbuilder.utils.VertexDecoderResult;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/mxgraph")
public class MxgraphController {

    private static final Logger LOG = LoggerFactory.getLogger("Launcher");

    @Autowired
    private ConversationsRepositories conversationsRepositories;

    //@Autowired 
    //private MxgraphmodelRepositories mxgraphmodelRepositories;
    @Autowired
    private FlowsRepositories flowRepositories;

    //@Autowired 
    //private FlowdraftRepositories flowdraftRepositories;
    @Autowired
    private VertexsRepositories vertexRepositories;

    @Autowired
    private MxgraphuserobjectRepositories mxUserObjectRepositories;

    @Autowired
    private MxgraphmxcellRepositories mxCellRepositories;

    @Autowired
    private MxgraphtriggerRepositories mxTriggerRepositories;

    @Autowired
    private MxgraphconditionRepositories mxConditionRepositories;

    @Autowired
    private MxgraphdatavariableRepositories mxDataVariableRepositories;

    @Autowired
    private MxgraphconnectionstartRepositories mxConnectionStartRepositories;

    @Autowired
    private MxgraphconnectionendRepositories mxConnectionEndRepositories;

    @Autowired
    private PublishuserobjectRepositories publishUserObjectRepositories;

    @Autowired
    private PublishmxcellRepositories publishMxCellRepositories;

    @Autowired
    private PublishtriggerRepositories publishTriggerRepositories;

    @Autowired
    private PublishconditionRepositories publishConditionRepositories;

    @Autowired
    private PublishdatavariableRepositories publishDataVariableRepositories;

    @Autowired
    private PublishconnectionstartRepositories publishConnectionStartRepositories;

    @Autowired
    private PublishconnectionendRepositories publishConnectionEndRepositories;

    @Value("${services.flow-core.delete-flow-converstaions.url:https://api.symplified.biz/flow-core/v1/flows/{{flowId}}/conversations}")
    String flowCoreDeleteConversationsUrl;

    @RequestMapping(method = RequestMethod.GET, value = "/{flow-id}", name = "mxgraph-get-by-flowId")
    @PreAuthorize("hasAnyAuthority('mxgraph-get-by-flowId', 'all')")
    public ResponseEntity<ProcessResult> getMxObject(
            @RequestHeader("Authorization") String auth,
            @PathVariable("flow-id") String flowId
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("-------------------getMx. Check token validity:" + auth);

            //retrieve userObject
            List<Mxgraphuserobject> userObjectList = mxUserObjectRepositories.getMxByFlowId(flowId);
            List<Object> userObjectArray = new ArrayList<>();
            for (int i = 0; i < userObjectList.size(); i++) {
                LOG.info("userobject[" + i + "]" + userObjectList.get(i).userObject.toString());
                userObjectArray.add(userObjectList.get(i).userObject);
            }

            //retrieve mxcell
            List<Mxgraphmxcell> mxcellList = mxCellRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> mxcellArray = new ArrayList<>();
            for (int i = 0; i < mxcellList.size(); i++) {
                LOG.info("mxcell[" + i + "]" + mxcellList.get(i).mxCell.toString());
                mxcellArray.add(mxcellList.get(i).mxCell);
            }

            //retrieve trigger
            List<Mxgraphtrigger> triggerList = mxTriggerRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> triggerArray = new ArrayList<>();
            for (int i = 0; i < triggerList.size(); i++) {
                LOG.info("mxcell[" + i + "]" + triggerList.get(i).triggers.toString());
                triggerArray.add(triggerList.get(i).triggers);
            }

            //retrieve data variable
            List<Mxgraphdatavariable> dataVariableList = mxDataVariableRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> dataVariableArray = new ArrayList<>();
            for (int i = 0; i < dataVariableList.size(); i++) {
                LOG.info("datavariable[" + i + "]" + dataVariableList.get(i).dataVariable.toString());
                dataVariableArray.add(dataVariableList.get(i).dataVariable);
            }

            //retrieve condition
            List<Mxgraphcondition> conditionList = mxConditionRepositories.getMxByFlowId(flowId);
            List<Object> conditionArray = new ArrayList<>();
            for (int i = 0; i < conditionList.size(); i++) {
                LOG.info("condition[" + i + "]" + conditionList.get(i).conditions.toString());
                conditionArray.add(conditionList.get(i).conditions);
            }

            //retrieve connection start
            List<Mxgraphconnectionstart> connectionStartList = mxConnectionStartRepositories.getMxByFlowId(flowId);
            List<Object> connectionStartArray = new ArrayList<>();
            for (int i = 0; i < connectionStartList.size(); i++) {
                LOG.info("condition[" + i + "]" + connectionStartList.get(i).connectionstarts.toString());
                connectionStartArray.add(connectionStartList.get(i).connectionstarts);
            }

            //retrieve connection end
            List<Mxgraphconnectionend> connectionEndList = mxConnectionEndRepositories.getMxByFlowId(flowId);
            List<Object> connectionEndArray = new ArrayList<>();
            for (int i = 0; i < connectionEndList.size(); i++) {
                LOG.info("condition[" + i + "]" + connectionEndList.get(i).connectionends.toString());
                connectionEndArray.add(connectionEndList.get(i).connectionends);
            }

            HashMap<String, Object> rootObject = new HashMap<>();
            if (mxcellArray.size() > 0) {
                rootObject.put("mxCell", mxcellArray);
            }
            if (userObjectArray.size() > 0) {
                rootObject.put("UserObject", userObjectArray);
            }
            if (triggerArray.size() > 0) {
                rootObject.put("triggers", triggerArray);
            }
            if (conditionArray.size() > 0) {
                rootObject.put("conditions", conditionArray);
            }
            if (connectionStartArray.size() > 0) {
                rootObject.put("ConnectionStart", connectionStartArray);
            }
            if (connectionEndArray.size() > 0) {
                rootObject.put("ConnectionEnd", connectionEndArray);
            }

            HashMap<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("root", rootObject);
            MxgraphmodelResponse mxgraph = new MxgraphmodelResponse();
            mxgraph.flowId = flowId;
            mxgraph.mxGraphModel = jsonResponse;
            if (dataVariableArray.size() > 0) {
                mxgraph.data = dataVariableArray;
            }
            if (mxgraph != null) {
                response.setData(mxgraph);
                response.setStatus(HttpStatus.OK.value());
            }

            LOG.info("[" + auth + "] getMx Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] getMx Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/publish/{flow-id}", name = "mxgraph-get-publish-by-flowId")
    @PreAuthorize("hasAnyAuthority('mxgraph-get-publish-by-flowId', 'all')")
    public ResponseEntity<ProcessResult> getPublishMxObject(
            @RequestHeader("Authorization") String auth,
            @PathVariable("flow-id") String flowId
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("-------------------getPublishMx. Check token validity:" + auth);

            //retrieve userObject
            List<Publishuserobject> userObjectList = publishUserObjectRepositories.getMxByFlowId(flowId);
            List<Object> userObjectArray = new ArrayList<>();
            for (int i = 0; i < userObjectList.size(); i++) {
                LOG.info("userobject[" + i + "]" + userObjectList.get(i).userObject.toString());
                userObjectArray.add(userObjectList.get(i).userObject);
            }

            //retrieve mxcell
            List<Publishmxcell> mxcellList = publishMxCellRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> mxcellArray = new ArrayList<>();
            for (int i = 0; i < mxcellList.size(); i++) {
                LOG.info("mxcell[" + i + "]" + mxcellList.get(i).mxCell.toString());
                mxcellArray.add(mxcellList.get(i).mxCell);
            }

            //retrieve trigger
            List<Publishtrigger> triggerList = publishTriggerRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> triggerArray = new ArrayList<>();
            for (int i = 0; i < triggerList.size(); i++) {
                LOG.info("mxcell[" + i + "]" + triggerList.get(i).triggers.toString());
                triggerArray.add(triggerList.get(i).triggers);
            }

            //retrieve data variable
            List<Publishdatavariable> dataVariableList = publishDataVariableRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> dataVariableArray = new ArrayList<>();
            for (int i = 0; i < dataVariableList.size(); i++) {
                LOG.info("datavariable[" + i + "]" + dataVariableList.get(i).dataVariable.toString());
                dataVariableArray.add(dataVariableList.get(i).dataVariable);
            }

            List<Publishcondition> conditionList = publishConditionRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> conditionArray = new ArrayList<>();
            for (int i = 0; i < conditionList.size(); i++) {
                LOG.info("condition[" + i + "]" + conditionList.get(i).conditions.toString());
                conditionArray.add(conditionList.get(i).conditions);
            }

            List<Publishconnectionstart> connectionStartList = publishConnectionStartRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> connectionStartArray = new ArrayList<>();
            for (int i = 0; i < connectionStartList.size(); i++) {
                LOG.info("connectionstart[" + i + "]" + connectionStartList.get(i).connectionstarts.toString());
                connectionStartArray.add(connectionStartList.get(i).connectionstarts);
            }

            List<Publishconnectionend> connectionEndList = publishConnectionEndRepositories.getMxByFlowId(flowId);
            //JSONArray userObjectArray = new JSONArray();
            List<Object> connectionEndArray = new ArrayList<>();
            for (int i = 0; i < connectionEndList.size(); i++) {
                LOG.info("connectionend[" + i + "]" + connectionEndList.get(i).connectionends.toString());
                connectionEndArray.add(connectionEndList.get(i).connectionends);
            }

            HashMap<String, Object> rootObject = new HashMap<>();
            if (mxcellArray.size() > 0) {
                rootObject.put("mxCell", mxcellArray);
            }
            if (userObjectArray.size() > 0) {
                rootObject.put("UserObject", userObjectArray);
            }
            if (triggerArray.size() > 0) {
                rootObject.put("triggers", triggerArray);
            }
            if (conditionArray.size() > 0) {
                rootObject.put("conditions", conditionArray);
            }
            if (connectionStartArray.size() > 0) {
                rootObject.put("ConnectionStart", connectionStartArray);
            }
            if (connectionEndArray.size() > 0) {
                rootObject.put("ConnectionEnd", connectionEndArray);
            }

            HashMap<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("root", rootObject);
            MxgraphmodelResponse mxgraph = new MxgraphmodelResponse();
            mxgraph.flowId = flowId;
            mxgraph.mxGraphModel = jsonResponse;
            if (dataVariableArray.size() > 0) {
                mxgraph.data = dataVariableArray;
            }
            if (mxgraph != null) {
                response.setData(mxgraph);
                response.setStatus(HttpStatus.OK.value());
            }

            LOG.info("[" + auth + "] getPublishMx Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] getPublishMx Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{flow-id}", name = "mxgraph-post-by-flowId")
    @PreAuthorize("hasAnyAuthority('mxgraph-post-by-flowId', 'all')")
    public ResponseEntity<ProcessResult> createAndUpdateMx(
            @RequestHeader("Authorization") String auth,
            @PathVariable("flow-id") String flowId,
            @RequestBody(required = true) String mxgraphmodelPayload
    ) {

        ProcessResult response = new ProcessResult();

        try {
            // This returns a JSON or XML with the users
            LOG.info("[" + auth + "] -------------------createMx. Check token validity:" + auth);

            LOG.info("[" + auth + "] createMx. Check flow-id:" + flowId);
            Optional<Flow> flow = flowRepositories.findById(flowId);
            if (!flow.isPresent()) {
                LOG.error("[" + auth + "] createMx FlowId not found");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<ProcessResult>(response, HttpStatus.NOT_FOUND);
            }

            LOG.info("[" + auth + "] createMx. MxgraphmodelPayload :" + mxgraphmodelPayload);

            //remove current mxgraph & vertex for current flow-id
            vertexRepositories.deleteVertexByFlowId(flowId);

            //Mxgraphmodel mxgraphmodel = new Mxgraphmodel();
            JsonObject gsonObj = new Gson().fromJson(mxgraphmodelPayload, JsonObject.class);

            LOG.debug("[" + auth + "] createMx. Converted into json:" + gsonObj.toString());

            //convert to vertex structure format
            VertexDecoderResult result = VertexDecoder.Decode(gsonObj);

            Vertex[] vertexList = result.vertexList;
            String topVertexId = "";
            Map<String, String> vertexMap = new HashMap<>();
            for (int x = 0; x < vertexList.length; x++) {
                Vertex vertex = vertexList[x];
                LOG.info("[" + auth + "] Vertex[" + x + "] Title = " + vertex.info.getTitle());
                LOG.info("[" + auth + "] Vertex[" + x + "] Text = " + vertex.info.getText());
                if (vertex.options != null) {
                    LOG.info("[" + auth + "] Vertex[" + x + "] Options = " + vertex.options.toString());
                }
                if (vertex.step != null) {
                    LOG.info("[" + auth + "] Vertex[" + x + "] Step = " + vertex.step.toString());
                }
                if (vertex.actions != null) {
                    LOG.info("[" + auth + "] Vertex[" + x + "] Actions = " + vertex.actions.toString());
                }
                vertex.flowId = flowId;
                vertexRepositories.save(vertex);
                LOG.info("[" + auth + "] createMx. Generated vertexId:" + vertex.id);
                if (vertex.isTopVertex == 1) {
                    topVertexId = vertex.id;
                }
                //stored new generated vertexId
                vertexMap.put(vertex.mxId, vertex.id);
            }

            //save as userObject object
            mxUserObjectRepositories.deleteUserObjectByFlowId(flowId);
            if (result.userObjectList != null) {
                Mxgraphuserobject[] userObjectList = result.userObjectList;
                for (int x = 0; x < userObjectList.length; x++) {
                    LOG.info("[" + auth + "] userObjectList[" + x + "] : " + userObjectList[x].toString());
                    userObjectList[x].flowId = flowId;
                    mxUserObjectRepositories.save(userObjectList[x]);
                }
            }

            //save as mxcell object
            mxCellRepositories.deleteMxcellByFlowId(flowId);
            if (result.mxCellList != null) {
                Mxgraphmxcell[] mxCellList = result.mxCellList;
                for (int x = 0; x < mxCellList.length; x++) {
                    LOG.info("[" + auth + "] mxCellList[" + x + "] : " + mxCellList[x].toString());
                    mxCellList[x].flowId = flowId;
                    mxCellRepositories.save(mxCellList[x]);
                }
            }

            //save as trigger object
            mxTriggerRepositories.deleteTriggerByFlowId(flowId);
            if (result.triggerList != null) {
                Mxgraphtrigger[] triggerList = result.triggerList;
                for (int x = 0; x < triggerList.length; x++) {
                    LOG.info("[" + auth + "] triggerList[" + x + "] : " + triggerList[x].toString());
                    triggerList[x].flowId = flowId;
                    mxTriggerRepositories.save(triggerList[x]);
                }
            }

            //save as data variable object
            mxDataVariableRepositories.deleteDataVariableByFlowId(flowId);
            if (result.dataVariableList != null) {
                Mxgraphdatavariable[] dataVariableList = result.dataVariableList;
                for (int x = 0; x < dataVariableList.length; x++) {
                    LOG.info("[" + auth + "] dataVariableList[" + x + "] : " + dataVariableList[x].toString());
                    dataVariableList[x].flowId = flowId;
                    mxDataVariableRepositories.save(dataVariableList[x]);
                }
            }

            //save as conditions object
            mxConditionRepositories.deleteConditionByFlowId(flowId);
            if (result.conditionList != null) {
                Mxgraphcondition[] conditionList = result.conditionList;
                for (int x = 0; x < conditionList.length; x++) {
                    LOG.info("[" + auth + "] conditionList[" + x + "] : " + conditionList[x].toString());
                    conditionList[x].flowId = flowId;
                    mxConditionRepositories.save(conditionList[x]);
                }
            }

            //save as connection start
            mxConnectionStartRepositories.deleteConnectionStartByFlowId(flowId);
            if (result.connectionStartList != null) {
                Mxgraphconnectionstart[] connectionStartList = result.connectionStartList;
                for (int x = 0; x < connectionStartList.length; x++) {
                    LOG.info("[" + auth + "] connectionStartList[" + x + "] : " + connectionStartList[x].toString());
                    connectionStartList[x].flowId = flowId;
                    mxConnectionStartRepositories.save(connectionStartList[x]);
                }
            }

            //save as connection end
            mxConnectionEndRepositories.deleteConnectionEndByFlowId(flowId);
            if (result.connectionEndList != null) {
                Mxgraphconnectionend[] connectionEndList = result.connectionEndList;
                for (int x = 0; x < connectionEndList.length; x++) {
                    LOG.info("[" + auth + "] connectionEndList[" + x + "] : " + connectionEndList[x].toString());
                    connectionEndList[x].flowId = flowId;
                    mxConnectionEndRepositories.save(connectionEndList[x]);
                }
            }

            //update targetId inside Step & Option.Step with new generated vertexId
            //soureId no need to update, not important to Core module
            for (int x = 0; x < vertexList.length; x++) {
                Vertex vertex = vertexList[x];
                if (vertex.step != null) {
                    Step step = vertex.step;
                    String targetId = step.getTargetId();
                    String newTargetId = vertexMap.get(targetId);
                    step.setTargetId(newTargetId);
                }
                if (vertex.options != null) {
                    for (int z = 0; z < vertex.options.size(); z++) {
                        Option option = vertex.options.get(z);
                        if (option.getStep() != null) {
                            Step step = option.getStep();
                            String targetId = step.getTargetId();
                            String newTargetId = vertexMap.get(targetId);
                            step.setTargetId(newTargetId);
                            //set option value = targetId
                            //option value get from button value
                            //option.setValue(newTargetId);
                        }
                    }
                    vertexRepositories.save(vertex);
                }
                /*if (vertex.dataVariables!=null) {
                             for (int z=0;z<vertex.options.size();z++) {
                                Option option = vertex.options.get(z);
                                if (option.getStep()!=null) {
                                    Step step = option.getStep();
                                    String targetId = step.getTargetId();
                                    String newTargetId = vertexMap.get(targetId);
                                    step.setTargetId(newTargetId);
                                }
                             }
                         }*/
            }

            //update topVertexId
            Flow currentFlow = flow.get();
            currentFlow.topVertexId = topVertexId;
            currentFlow.status = "draft";

            flowRepositories.save(currentFlow);

            LOG.info("[" + auth + "] createMx Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] createMx Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{flow-id}", name = "mxgraph-delete-by-flowId")
    @PreAuthorize("hasAnyAuthority('mxgraph-delete-by-flowId', 'all')")
    public ResponseEntity<ProcessResult> deleteMx(
            @RequestHeader("Authorization") String auth,
            @PathVariable("flowId") String flowId
    ) {

        ProcessResult response = new ProcessResult();
        try {
            // This returns a JSON or XML with the users
            LOG.info("-------------------deleteMx. Check token validity:" + auth);
            //mxgraphmodelRepositories.deleteMxgraphByFlowId(flowId);  
            mxCellRepositories.deleteMxcellByFlowId(flowId);
            mxUserObjectRepositories.deleteUserObjectByFlowId(flowId);
            mxTriggerRepositories.deleteTriggerByFlowId(flowId);
            mxConditionRepositories.deleteConditionByFlowId(flowId);
            mxConnectionStartRepositories.deleteConnectionStartByFlowId(flowId);
            mxConnectionEndRepositories.deleteConnectionEndByFlowId(flowId);
            LOG.info("[" + auth + "] deleteMx Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] deleteMx Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{action-type}/{flow-id}", name = "mxgraph-path-by-actionType-flowId")
    @PreAuthorize("hasAnyAuthority('mxgraph-path-by-actionType-flowId', 'all')")
    public ResponseEntity<ProcessResult> autoSaveMx(
            @RequestHeader("Authorization") String auth,
            @PathVariable("flow-id") String flowId,
            @PathVariable("action-type") String actionType,
            @RequestBody(required = false) String mxObject
    ) {

        ProcessResult response = new ProcessResult();
        try {

            // This returns a JSON or XML with the users
            LOG.info("[" + auth + "] -------------------autoSaveMx. Check token validity:" + auth);
            LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " actionType:" + actionType + " mxObject :" + mxObject);

            if (actionType.equalsIgnoreCase("publish")) {

                //delete all conversations related to this flow in flow core
                RestTemplate restTemplate = new RestTemplate();

                try {
                    String url = flowCoreDeleteConversationsUrl.replace("{{flowId}}", flowId);
                    LOG.info("[" + auth + "] autoSaveMx. all conversations deleted for flowCoreDeleteConversationsUrl: " + flowCoreDeleteConversationsUrl);

                    LOG.info("[" + auth + "] autoSaveMx. all conversations deleted for url: " + url);

                    restTemplate.delete(url);

//                    if (res.getStatusCode() == HttpStatus.OK) {
//                        LOG.info("[" + auth + "] autoSaveMx. all conversations deleted for flowId: " + flowId);
//                    } else {
//                        LOG.info("[" + auth + "] autoSaveMx. existing conversations could not be deleted res: " + res.toString());
//
//                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//                        response.setMessage("Existing conversations could not be deleted");
//                        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//                    }
                } catch (Exception e) {
                    LOG.error("[" + auth + "] autoSaveMx. existing conversations could not be deleted res", e);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setMessage("Existing conversations could not be deleted");
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                //remove current mxgraph & vertex for current flow-id
                vertexRepositories.deleteVertexByFlowId(flowId);
                //this part should be in publish, when user complete editing
                LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Publish flow");
                //retrieve back all struture
                JsonObject mxGraphModelJson = RetrieveMxObject(flowId);
                //convert to vertex structure format
                VertexDecoderResult result = VertexDecoder.Decode(mxGraphModelJson);
                //save as vertex stucture format
                Vertex[] vertexList = result.vertexList;
                String topVertexId = "";
                Map<String, String> vertexMap = new HashMap<>();
                for (int x = 0; x < vertexList.length; x++) {
                    Vertex vertex = vertexList[x];

                    LOG.info("[" + auth + "] Vertex[" + x + "] vertex = " + vertex.toString());

                    if (vertex.options != null) {
                        LOG.info("[" + auth + "] Vertex[" + x + "] Options = " + vertex.options.toString());
                    }
                    if (vertex.step != null) {
                        LOG.info("[" + auth + "] Vertex[" + x + "] Step = " + vertex.step.toString());
                    }
                    if (vertex.actions != null) {
                        LOG.info("[" + auth + "] Vertex[" + x + "] Actions = " + vertex.actions.toString());
                    }
                    vertex.flowId = flowId;
                    vertexRepositories.save(vertex);
                    LOG.info("[" + auth + "] createMx. Generated vertexId:" + vertex.id);
                    if (vertex.isTopVertex == 1) {
                        topVertexId = vertex.id;
                    }
                    //stored new generated vertexId
                    vertexMap.put(vertex.mxId, vertex.id);
                }

                //update targetId inside Step & Option.Step with new generated vertexId
                //soureId no need to update, not important to Core module
                for (int x = 0; x < vertexList.length; x++) {
                    Vertex vertex = vertexList[x];
                    if (vertex.step != null) {
                        Step step = vertex.step;
                        String targetId = step.getTargetId();
                        String newTargetId = vertexMap.get(targetId);
                        step.setTargetId(newTargetId);
                    }
                    if (vertex.options != null) {
                        for (int z = 0; z < vertex.options.size(); z++) {
                            Option option = vertex.options.get(z);
                            if (option.getStep() != null) {
                                Step step = option.getStep();
                                String targetId = step.getTargetId();
                                String newTargetId = vertexMap.get(targetId);
                                step.setTargetId(newTargetId);
                                //set option value = targetId
                                //option.setValue(newTargetId);
                            }
                        }
                    }
                    //store vertex in db
                    vertexRepositories.save(vertex);
                }

                //update topVertexId
                Optional<Flow> flowList = flowRepositories.findById(flowId);
                if (flowList.isPresent()) {
                    Flow currentFlow = flowList.get();
                    currentFlow.topVertexId = topVertexId;
                    currentFlow.status = "publish";
                    flowRepositories.save(currentFlow);
                    LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Flow updated to publish");
                } else {
                    LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Flow not found");
                }

                //copy object to publish collection
                PublishMxObject(flowId);
                LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Mx Object copied to publish");

                //update botIds
                JsonObject mainJsonObj = new Gson().fromJson(mxObject, JsonObject.class);
                LOG.info("[" + auth + "] botIds:" + mainJsonObj.toString());

                JsonArray botIds = new JsonArray();
                if (mainJsonObj.get("botIds") != null) {
                    botIds = mainJsonObj.get("botIds").getAsJsonArray();
                }

                if (botIds.size() > 0) {
                    String[] botIdArray = new String[botIds.size()];
                    for (int i = 0; i < botIds.size(); i++) {
                        LOG.info("[" + auth + "]  botIds:" + botIds.get(i).toString());
                        botIdArray[i] = botIds.get(i).getAsString();
                        //remove botId in other flow
                        List<Flow> foundFlowList = flowRepositories.getByBotIds(botIdArray[i]);
                        LOG.info("[" + auth + "] Found flow with botId:" + botIdArray[i] + " : " + foundFlowList.size());
                        for (int x = 0; x < foundFlowList.size(); x++) {
                            Flow foundFlow = foundFlowList.get(x);
                            String[] newBotIds = new String[foundFlow.botIds.length - 1];
                            int f = 0;
                            for (int y = 0; y < foundFlow.botIds.length; y++) {
                                if (!foundFlow.botIds[y].equals(botIdArray[i])) {
                                    newBotIds[f] = foundFlow.botIds[y];
                                    f++;
                                }
                            }
                            LOG.info("[" + auth + "] foundFlow:" + foundFlow.id + " newBotIds:" + newBotIds.toString());
                            foundFlow.botIds = newBotIds;
                            flowRepositories.save(foundFlow);
                        }
                    }
                    //update botId into flow
                    Optional<Flow> flowList2 = flowRepositories.findById(flowId);
                    if (flowList2.isPresent()) {
                        Flow currentFlow = flowList2.get();
                        currentFlow.botIds = botIdArray;
                        flowRepositories.save(currentFlow);
                        LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Flow Botids updated");
                    } else {
                        LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Flow not found");
                    }
                }
            } else {
                LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Update mxobject. Action type:" + actionType);
                JsonObject mainJsonObj = new Gson().fromJson(mxObject, JsonObject.class);
                for (Map.Entry<String, JsonElement> entry : mainJsonObj.entrySet()) {
                    String mainObjectType = entry.getKey();
                    LOG.info("[" + auth + "] mainDynamicKey:" + mainObjectType);
                    if (mainObjectType.equalsIgnoreCase("object")) {
                        if (actionType.equalsIgnoreCase("DELETE")) {
                            LOG.info("[" + auth + "] DELETE object");
                            JsonArray arrayValue = mainJsonObj.get(mainObjectType).getAsJsonArray();
                            for (int i = 0; i < arrayValue.size(); i++) {
                                JsonObject jsonObj = arrayValue.get(i).getAsJsonObject();
                                for (Map.Entry<String, JsonElement> subEntry : jsonObj.entrySet()) {
                                    String objectType = subEntry.getKey();
                                    LOG.info("[" + auth + "] subDynamicKey delete objectType:" + objectType);
                                    if (objectType.equalsIgnoreCase("UserObject")) {
                                        JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                        Mxgraphuserobject.UpdateUserObject(flowId, actionType, objectValue, mxUserObjectRepositories, mxCellRepositories, mxTriggerRepositories);
                                    } else if (objectType.equalsIgnoreCase("triggers")) {
                                        JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                        Mxgraphtrigger.UpdateTrigger(flowId, actionType, objectValue, mxTriggerRepositories, mxConnectionStartRepositories, mxConnectionEndRepositories);
                                    } else if (objectType.equalsIgnoreCase("mxcell")) {
                                        JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                        Mxgraphmxcell.UpdateMxcell(flowId, actionType, objectValue, mxCellRepositories);
                                    } else if (objectType.equalsIgnoreCase("conditions")) {
                                        JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                        Mxgraphcondition.UpdateCondition(flowId, actionType, objectValue, mxConditionRepositories);
                                    } else if (objectType.equalsIgnoreCase("ConnectionStart")) {
                                        JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                        Mxgraphconnectionstart.UpdateConnectionStart(flowId, actionType, objectValue, mxConnectionStartRepositories);
                                    } else if (objectType.equalsIgnoreCase("ConnectionEnd")) {
                                        JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                        Mxgraphconnectionend.UpdateConnectionEnd(flowId, actionType, objectValue, mxConnectionEndRepositories);
                                    }
                                }
                            }
                        } else {
                            JsonObject jsonObj = mainJsonObj.get(mainObjectType).getAsJsonObject();
                            for (Map.Entry<String, JsonElement> subEntry : jsonObj.entrySet()) {
                                String objectType = subEntry.getKey();
                                LOG.info("[" + auth + "] subDynamicKey:" + objectType);
                                if (objectType.equalsIgnoreCase("UserObject")) {
                                    JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                    Mxgraphuserobject.UpdateUserObject(flowId, actionType, objectValue, mxUserObjectRepositories, mxCellRepositories, mxTriggerRepositories);
                                } else if (objectType.equalsIgnoreCase("triggers")) {
                                    JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                    Mxgraphtrigger.UpdateTrigger(flowId, actionType, objectValue, mxTriggerRepositories, mxConnectionStartRepositories, mxConnectionEndRepositories);
                                } else if (objectType.equalsIgnoreCase("mxcell")) {
                                    JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                    Mxgraphmxcell.UpdateMxcell(flowId, actionType, objectValue, mxCellRepositories);
                                } else if (objectType.equalsIgnoreCase("conditions")) {
                                    JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                    Mxgraphcondition.UpdateCondition(flowId, actionType, objectValue, mxConditionRepositories);
                                } else if (objectType.equalsIgnoreCase("ConnectionStart")) {
                                    JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                    Mxgraphconnectionstart.UpdateConnectionStart(flowId, actionType, objectValue, mxConnectionStartRepositories);
                                } else if (objectType.equalsIgnoreCase("ConnectionEnd")) {
                                    JsonObject objectValue = jsonObj.get(objectType).getAsJsonObject();
                                    Mxgraphconnectionend.UpdateConnectionEnd(flowId, actionType, objectValue, mxConnectionEndRepositories);
                                }
                            }
                        }
                    } else if (mainObjectType.equalsIgnoreCase("data")) {
                        JsonArray arrayValue = mainJsonObj.get(mainObjectType).getAsJsonArray();
                        for (int i = 0; i < arrayValue.size(); i++) {
                            JsonObject jsonObject = arrayValue.get(i).getAsJsonObject();
                            String vertexId = jsonObject.get("vertexId").getAsString();
                            Mxgraphdatavariable.UpdateDataVariable(flowId, vertexId, actionType, jsonObject, mxDataVariableRepositories);
                        }
                    }

                }

                //update topVertexId
                Optional<Flow> flowList = flowRepositories.findById(flowId);
                if (flowList.isPresent()) {
                    Flow currentFlow = flowList.get();
                    currentFlow.status = "draft";
                    flowRepositories.save(currentFlow);
                    LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Flow updated to draft");
                } else {
                    LOG.info("[" + auth + "] autoSaveMx. flowId:" + flowId + " Flow not found");
                }
            }

            LOG.info("[" + auth + "] autoSaveMx Finish");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exp) {
            LOG.error("[" + auth + "] autoSaveMx Execption :", exp);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private JsonObject RetrieveMxObject(String flowId) {
        //retrieve userObject
        List<Mxgraphuserobject> userObjectList = mxUserObjectRepositories.getMxByFlowId(flowId);
        List<Object> userObjectArray = new ArrayList<>();
        for (int i = 0; i < userObjectList.size(); i++) {
            LOG.info("userobject[" + i + "]" + userObjectList.get(i).userObject.toString());
            userObjectArray.add(userObjectList.get(i).userObject);
        }

        //retrieve mxcell
        List<Mxgraphmxcell> mxcellList = mxCellRepositories.getMxByFlowId(flowId);
        //JSONArray userObjectArray = new JSONArray();
        List<Object> mxcellArray = new ArrayList<>();
        for (int i = 0; i < mxcellList.size(); i++) {
            LOG.info("mxcell[" + i + "]" + mxcellList.get(i).mxCell.toString());
            mxcellArray.add(mxcellList.get(i).mxCell);
        }

        //retrieve trigger
        List<Mxgraphtrigger> triggerList = mxTriggerRepositories.getMxByFlowId(flowId);
        //JSONArray userObjectArray = new JSONArray();
        List<Object> triggerArray = new ArrayList<>();
        for (int i = 0; i < triggerList.size(); i++) {
            LOG.info("trigger[" + i + "]" + triggerList.get(i).triggers.toString());
            triggerArray.add(triggerList.get(i).triggers);
        }

        //retrieve dataVariable
        List<Mxgraphdatavariable> dataVariableList = mxDataVariableRepositories.getMxByFlowId(flowId);
        //JSONArray userObjectArray = new JSONArray();
        List<Object> dataVariableArray = new ArrayList<>();
        for (int i = 0; i < dataVariableList.size(); i++) {
            LOG.info("dataVariable[" + i + "]" + dataVariableList.get(i).dataVariable.toString());
            dataVariableArray.add(dataVariableList.get(i).dataVariable);
        }

        //retrieve condition
        List<Mxgraphcondition> conditionList = mxConditionRepositories.getMxByFlowId(flowId);
        //JSONArray userObjectArray = new JSONArray();
        List<Object> conditionArray = new ArrayList<>();
        for (int i = 0; i < conditionList.size(); i++) {
            LOG.info("conditions[" + i + "]" + conditionList.get(i).conditions.toString());
            conditionArray.add(conditionList.get(i).conditions);
        }

        //retrieve connection start
        List<Mxgraphconnectionstart> connectionStartList = mxConnectionStartRepositories.getMxByFlowId(flowId);
        //JSONArray userObjectArray = new JSONArray();
        List<Object> connectionStartArray = new ArrayList<>();
        for (int i = 0; i < connectionStartList.size(); i++) {
            LOG.info("connectionstart[" + i + "]" + connectionStartList.get(i).connectionstarts.toString());
            connectionStartArray.add(connectionStartList.get(i).connectionstarts);
        }

        //retrieve connnection end
        List<Mxgraphconnectionend> connectionEndList = mxConnectionEndRepositories.getMxByFlowId(flowId);
        //JSONArray userObjectArray = new JSONArray();
        List<Object> connectionEndArray = new ArrayList<>();
        for (int i = 0; i < connectionEndList.size(); i++) {
            LOG.info("connectionend[" + i + "]" + connectionEndList.get(i).connectionends.toString());
            connectionEndArray.add(connectionEndList.get(i).connectionends);
        }

        HashMap<String, Object> rootObject = new HashMap<>();
        rootObject.put("UserObject", userObjectArray);
        rootObject.put("triggers", triggerArray);
        rootObject.put("mxCell", mxcellArray);
        rootObject.put("conditions", conditionArray);
        rootObject.put("ConnectionStart", connectionStartArray);
        rootObject.put("ConnectionEnd", connectionEndArray);

        HashMap<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("root", rootObject);

        HashMap<String, Object> finalJsonResponse = new HashMap<>();
        finalJsonResponse.put("mxGraphModel", jsonResponse);
        finalJsonResponse.put("data", dataVariableArray);

        String jsonString = new Gson().toJson(finalJsonResponse);
        JsonObject resp = new Gson().fromJson(jsonString, JsonObject.class);

        return resp;
    }

    private void PublishMxObject(String flowId) {
        //remove all publish object
        publishUserObjectRepositories.deleteUserObjectByFlowId(flowId);//userObject
        publishMxCellRepositories.deleteMxcellByFlowId(flowId);//mxcell
        publishTriggerRepositories.deleteTriggerByFlowId(flowId);//trigger
        publishDataVariableRepositories.deleteDataVariableByFlowId(flowId);//dataVariable
        publishConditionRepositories.deleteConditionByFlowId(flowId);//dataVariable
        publishConnectionStartRepositories.deleteConnectionStartByFlowId(flowId);//dataVariable
        publishConnectionEndRepositories.deleteConnectionEndByFlowId(flowId);//dataVariable
        LOG.info("all publish mx object deleted for flowId:" + flowId);

        //retrieve userObject
        List<Mxgraphuserobject> userObjectList = mxUserObjectRepositories.getMxByFlowId(flowId);
        for (int i = 0; i < userObjectList.size(); i++) {
            Mxgraphuserobject mxgraphuserobject = userObjectList.get(i);
            Publishuserobject publishuserobject = new Publishuserobject();
            publishuserobject.flowId = mxgraphuserobject.flowId;
            publishuserobject.mxId = mxgraphuserobject.mxId;
            publishuserobject.userObject = mxgraphuserobject.userObject;
            publishUserObjectRepositories.save(publishuserobject);
            LOG.info("userobject[" + i + "] copied to publish");
        }

        //retrieve mxcell
        List<Mxgraphmxcell> mxcellList = mxCellRepositories.getMxByFlowId(flowId);
        for (int i = 0; i < mxcellList.size(); i++) {
            Mxgraphmxcell mxgraphmxcell = mxcellList.get(i);
            Publishmxcell publishmxcell = new Publishmxcell();
            publishmxcell.flowId = mxgraphmxcell.flowId;
            publishmxcell.mxId = mxgraphmxcell.mxId;
            publishmxcell.mxCell = mxgraphmxcell.mxCell;
            publishMxCellRepositories.save(publishmxcell);
            LOG.info("mxcell[" + i + "] copied to publish");
        }

        //retrieve trigger
        List<Mxgraphtrigger> triggerList = mxTriggerRepositories.getMxByFlowId(flowId);
        for (int i = 0; i < triggerList.size(); i++) {
            Mxgraphtrigger mxgraphtrigger = triggerList.get(i);
            Publishtrigger publishtrigger = new Publishtrigger();
            publishtrigger.flowId = mxgraphtrigger.flowId;
            publishtrigger.mxId = mxgraphtrigger.mxId;
            publishtrigger.triggers = mxgraphtrigger.triggers;
            publishTriggerRepositories.save(publishtrigger);
            LOG.info("trigger[" + i + "] copied to publish");
        }

        //retrieve dataVariable
        List<Mxgraphdatavariable> dataVariableList = mxDataVariableRepositories.getMxByFlowId(flowId);
        for (int i = 0; i < dataVariableList.size(); i++) {
            Mxgraphdatavariable mxgraphdatavariable = dataVariableList.get(i);
            Publishdatavariable publishdatavariable = new Publishdatavariable();
            publishdatavariable.flowId = mxgraphdatavariable.flowId;
            publishdatavariable.dataVariable = mxgraphdatavariable.dataVariable;
            publishdatavariable.vertexId = mxgraphdatavariable.vertexId;
            publishDataVariableRepositories.save(publishdatavariable);
            LOG.info("dataVariable[" + i + "] copied to publish");
        }

        //retrieve conditions
        List<Mxgraphcondition> conditionList = mxConditionRepositories.getMxByFlowId(flowId);
        for (int i = 0; i < conditionList.size(); i++) {
            Mxgraphcondition mxgraphcondition = conditionList.get(i);
            Publishcondition publishcondition = new Publishcondition();
            publishcondition.flowId = mxgraphcondition.flowId;
            publishcondition.conditions = mxgraphcondition.conditions;
            publishConditionRepositories.save(publishcondition);
            LOG.info("condition[" + i + "] copied to publish");
        }

        //retrieve connection start
        List<Mxgraphconnectionstart> connectionStartList = mxConnectionStartRepositories.getMxByFlowId(flowId);
        for (int i = 0; i < connectionStartList.size(); i++) {
            Mxgraphconnectionstart mxgraphconnectionstart = connectionStartList.get(i);
            Publishconnectionstart connectionStart = new Publishconnectionstart();
            connectionStart.flowId = mxgraphconnectionstart.flowId;
            connectionStart.connectionstarts = mxgraphconnectionstart.connectionstarts;
            publishConnectionStartRepositories.save(connectionStart);
            LOG.info("connectionStart[" + i + "] copied to publish");
        }

        //retrieve connection end
        List<Mxgraphconnectionend> connectionEndList = mxConnectionEndRepositories.getMxByFlowId(flowId);
        for (int i = 0; i < connectionEndList.size(); i++) {
            Mxgraphconnectionend mxgraphconnectionend = connectionEndList.get(i);
            Publishconnectionend connectionEnd = new Publishconnectionend();
            connectionEnd.flowId = mxgraphconnectionend.flowId;
            connectionEnd.connectionends = mxgraphconnectionend.connectionends;
            publishConnectionEndRepositories.save(connectionEnd);
            LOG.info("connectionEnd[" + i + "] copied to publish");
        }

    }
}
