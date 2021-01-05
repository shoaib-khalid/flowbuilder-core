package com.kalsym.chatbot.flowbuilder.controllers;

import java.util.List;
import java.util.Optional;
import org.json.JSONObject;
import org.json.JSONArray;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kalsym.chatbot.flowbuilder.ProcessResult;
import com.kalsym.chatbot.flowbuilder.mxmodel.Mxgraphmodel;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import com.kalsym.chatbot.flowbuilder.models.daos.Flow;
import com.kalsym.chatbot.flowbuilder.models.MxgraphmodelPayload;
import com.kalsym.chatbot.flowbuilder.models.enums.VertexTargetType;
import com.kalsym.chatbot.flowbuilder.submodels.Option;
import com.kalsym.chatbot.flowbuilder.submodels.Step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.kalsym.chatbot.flowbuilder.repositories.MxgraphmodelRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.VertexsRepositories;
import com.kalsym.chatbot.flowbuilder.repositories.FlowsRepositories;
import com.kalsym.chatbot.flowbuilder.utils.VertexDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sarosh
 */
@RestController()
@RequestMapping("/mxgraph")
public class MxgraphController {
   
    private static final Logger LOG = LoggerFactory.getLogger("Launcher");
    
    @Autowired 
    private MxgraphmodelRepositories mxgraphmodelRepositories;
    
    @Autowired 
    private FlowsRepositories flowRepositories;
    
    @Autowired 
    private VertexsRepositories vertexRepositories;
    
            
   @RequestMapping(method= RequestMethod.GET, value="/{flow-id}")
	public ResponseEntity<ProcessResult> getMx(
                @RequestHeader("Authorization") String auth,
                @PathVariable("flow-id") String flowId
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("getMx. Check token validity:"+auth); 
                    
                    Mxgraphmodel mxgraph = mxgraphmodelRepositories.getMxByFlowId(flowId);
                    if (mxgraph!=null) {
                        response.setData(mxgraph);
                        response.setStatus(HttpStatus.OK.value());
                    }

                    LOG.info("["+auth+"] getMx Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] getMx Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
    @RequestMapping(method= RequestMethod.POST, value="/{flow-id}")
	public ResponseEntity<ProcessResult> createAndUpdateMx(
                @RequestHeader("Authorization") String auth,
                @PathVariable("flow-id") String flowId,
                @RequestBody(required = true) MxgraphmodelPayload mxgraphmodelPayload
                ) {
		
                ProcessResult response = new ProcessResult();
           
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("["+auth+"] createMx. Check token validity:"+auth);
                    
                    LOG.info("["+auth+"] createMx. Check flow-id:"+flowId);
                    Optional<Flow> flow = flowRepositories.findById(flowId);
                    if (!flow.isPresent()) {
                        LOG.error("["+auth+"] createMx FlowId not found");
                        response.setStatus(HttpStatus.NOT_FOUND.value());
                        return new ResponseEntity<ProcessResult>(response, HttpStatus.NOT_FOUND);
                    }   
                    
                    LOG.info("["+auth+"] createMx. MxgraphmodelPayload :"+mxgraphmodelPayload.toString());                    
                    
                    //remove current mxgraph & vertex for current flow-id
                    mxgraphmodelRepositories.deleteMxgraphByFlowId(flowId);
                    vertexRepositories.deleteVertexByFlowId(flowId);
                            
                    Mxgraphmodel mxgraphmodel = new Mxgraphmodel();
                    JSONObject jsonObj = new JSONObject(mxgraphmodelPayload);
                    Object dataObject = BasicDBObject.parse(jsonObj.getJSONObject("mxGraphModel").toString());
                    mxgraphmodel.mxGraphModel = (DBObject)dataObject;
                    mxgraphmodel.flowId = flowId;
                    //store mxgraph in original format
                    mxgraphmodelRepositories.save(mxgraphmodel);
                    
                    //convert to vertex structure format
                    Vertex[] vertexList = VertexDecoder.Decode(jsonObj.getJSONObject("mxGraphModel"));
                    //vertexRepositories.save(vertex);
                    
                    String topVertexId = "";
                    Map<String, String> vertexMap = new HashMap<>();
                    for (int x=0;x<vertexList.length;x++) {
                        Vertex vertex = vertexList[x];
                        LOG.info("["+auth+"] Vertex["+x+"] Title = "+vertex.info.getTitle());
                        LOG.info("["+auth+"] Vertex["+x+"] Text = "+vertex.info.getText());                        
                        LOG.info("["+auth+"] Vertex["+x+"] Options = "+vertex.options.toString());
                        //LOG.info("["+auth+"] Vertex["+x+"] Step = "+vertex.step.toString()); 
                        //LOG.info("["+auth+"] Vertex["+x+"] Actions = "+vertex.actions.toString());
                        vertex.flowId=flowId;                       
                        vertexRepositories.save(vertex);
                        LOG.info("["+auth+"] createMx. Generated vertexId:"+vertex.id); 
                        if (vertex.isTopVertex==1) {
                            topVertexId = vertex.id;
                        }
                        vertexMap.put(vertex.mxId, vertex.id);
                    }
                    
                    //update vertexId inside action
                    for (int x=0;x<vertexList.length;x++) {
                         Vertex vertex = vertexList[x];
                         if (vertex.step!=null) {
                             Step step = vertex.step;
                             String targetId = step.getTargetId();
                             String newTargetId = vertexMap.get(targetId);
                             step.setTargetId(newTargetId);
                         }
                         if (vertex.options!=null) {
                             for (int z=0;z<vertex.options.size();z++) {
                                Option option = vertex.options.get(z);
                                if (option.getStep()!=null) {
                                    Step step = option.getStep();
                                    String targetId = step.getTargetId();
                                    String newTargetId = vertexMap.get(targetId);
                                    step.setTargetId(newTargetId);
                                }
                             }
                             vertexRepositories.save(vertex);
                         }
                    }
                     
                    //update topVertexId
                    Flow currentFlow = flow.get();
                    currentFlow.topVertexId = topVertexId;
                    flowRepositories.save(currentFlow);
                    
                    LOG.info("["+auth+"] createMx Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] createMx Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
        
    @RequestMapping(method= RequestMethod.DELETE, value="/{flow-id}")
	public ResponseEntity<ProcessResult> deleteMx(
                @RequestHeader("Authorization") String auth,
                @PathVariable("id") String id
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("deleteMx. Check token validity:"+auth);                     
                    mxgraphmodelRepositories.deleteById(id);                   
                    LOG.info("["+auth+"] deleteMx Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] deleteMx Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
        
        
   /* @RequestMapping(method= RequestMethod.PATCH, value="/{flow-id}")
	public ResponseEntity<ProcessResult> updateMx(
                @RequestHeader("Authorization") String auth,
                @PathVariable("id") String id,
                @RequestBody(required = true) String mxgraphmodelPayload
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("["+auth+"] updateMx. Check token validity:"+auth);                     
                    LOG.info("["+auth+"] updateMx. FlowPayload :"+mxgraphmodelPayload.toString()); 
                    
                    Mxgraphmodel mxgraphmodel = new Mxgraphmodel();
                    JSONObject jsonObj = new JSONObject(mxgraphmodelPayload);
                    Object dataObject = BasicDBObject.parse(jsonObj.getJSONObject("data").toString());
                    mxgraphmodel.mxGraphModel = (DBObject)dataObject;
                    mxgraphmodel.flowId = jsonObj.getString("flowId");
                    mxgraphmodel.id = id;
                    mxgraphmodelRepositories.save(mxgraphmodel);
                    
                    LOG.info("["+auth+"] updateMx Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] updateMx Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}
    */
        
        /*@RequestMapping(method= RequestMethod.GET, value="/test")
	public ResponseEntity<ProcessResult> test(
                @RequestHeader("Authorization") String auth
                ) {
		
                ProcessResult response = new ProcessResult();
                try {
                    // This returns a JSON or XML with the users
                    LOG.info("getMx. Check token validity:"+auth); 
                    
                    List<Mxgraphmodel> flowList = mxgraphmodelRepositories.findAll();
                    for (int i=0; i< flowList.size(); i++) {
                        Mxgraphmodel mx = flowList.get(i);                        
                        JSONObject jsonData = new JSONObject(mx.mxGraphModel.toString());
                        Vertex[] vertexList = VertexDecoder.Decode(jsonData);
                        for (int x=0;x<vertexList.length;x++) {
                            Vertex v = vertexList[x];
                            LOG.info("Vertex["+x+"] ");
                            LOG.info("   id="+v.id);
                            for (int z=0;z<v.options.size();z++) {
                                Option opt = v.options.get(z);
                                LOG.info("   option["+z+"] -> id:"+opt.getId()+" text:"+opt.getText());                                        
                            }
                            
                        }
                    }
                    
                    if (!flowList.isEmpty()) {
                        response.setData(flowList);
                        response.setStatus(HttpStatus.OK.value());
                    }

                    LOG.info("["+auth+"] getMx Finish");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception exp) {
                    LOG.error("["+auth+"] getMx Execption :",exp);
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return new ResponseEntity<ProcessResult>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
	}*/
        
}
