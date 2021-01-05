/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.utils;

import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import com.kalsym.chatbot.flowbuilder.models.enums.VertexTargetType;
import com.kalsym.chatbot.flowbuilder.submodels.Option;
import com.kalsym.chatbot.flowbuilder.submodels.Step;
import com.kalsym.chatbot.flowbuilder.submodels.Info;
import com.kalsym.chatbot.flowbuilder.mxmodel.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class VertexDecoder {
    
    private static final Logger LOG = LoggerFactory.getLogger("application");
     
    public static Vertex[] Decode(JSONObject json) {
        JSONObject jsondata = json.getJSONObject("root");
        JSONArray mxcell = extractJSONArray(jsondata,"mxCell");
        JSONArray userobject = extractJSONArray(jsondata,"UserObject");
        JSONArray triggers = extractJSONArray(jsondata,"triggers");
        JSONArray initialMessages = extractJSONArray(jsondata,"InitialMessage");
        
        Map<String, MxObject> objectMap = new HashMap<>();
        
        LOG.info("mxcell:"+mxcell.toString());
        int mxcellSize = mxcell.length();
        Map<String, Step> stepMap = new HashMap<>();
        for (int i=0;i<mxcellSize;i++) {
            JSONObject cell = (JSONObject)mxcell.get(i);
            String id = cell.getString("@id");
            LOG.info("Cell["+i+"] -> id="+id);
            String isEdge = extractStringFromJson(cell,"@edge");
            if (isEdge.equals("1")) {                    
                String parentId = cell.getString("@parent");
                String sourceTriggerId = cell.getString("@source");
                String targetVertexId = cell.getString("@target");
                LOG.info("Edge["+i+"] -> sourceTriggerId="+sourceTriggerId+" targetVertexId="+targetVertexId);
                Step step = new Step();
                step.setTargetId(targetVertexId);
                step.setSourceId(sourceTriggerId);
                stepMap.put(sourceTriggerId, step);

                Edge edge = new Edge();
                edge.setId(id);
                edge.setParent(parentId);
                edge.setSource(sourceTriggerId);
                edge.setTarget(targetVertexId);
                MxObject mxObject = new MxObject(MxObjectType.EDGE, edge);
                objectMap.put(id, mxObject);
            } else {               
                String isVertex = extractStringFromJson(cell,"@vertex");
                if (isVertex.equalsIgnoreCase("1")) {
                    String parentId = cell.getString("@parent");
                    String value = cell.getString("@value");                            
                    Anchor anchor = new Anchor();
                    anchor.setId(id);
                    anchor.setParent(parentId);
                    anchor.setValue(value);
                    MxObject mxObject = new MxObject(MxObjectType.ANCHOR, anchor);
                    objectMap.put(id, mxObject);
                }
            }
        }
        
        LOG.info("userobject:"+userobject.toString());
        int userSize = userobject.length();
        Map<String, Vertex> vertexMap = new HashMap<>();       
        for (int i=0;i<userSize;i++) {
            JSONObject cell = (JSONObject)userobject.get(i);
            String id = cell.getString("@id");
            LOG.info("UserObject["+i+"] -> id="+id);
            
            JSONObject t1 = cell.getJSONObject("mxCell").getJSONObject("div").getJSONObject("div");
            JSONArray a1 = t1.getJSONArray("div");
            JSONObject t2 = GetCorrectObjectFromDiv(a1, "card");
            LOG.info("UserObject["+i+"] -> t2="+t2.toString());
            JSONArray a2 = t2.getJSONArray("div");
            JSONObject t3 = GetCorrectObjectFromDiv(a2, "card-header");
            LOG.info("UserObject["+i+"] -> t3="+t3.toString());
            String title=t3.getJSONObject("div").getJSONObject("h4").getString("#text");
            LOG.info("UserObject["+i+"] -> title="+title);
            
            Vertex vertex = new Vertex();
            vertex.mxId = id;
            Info info = new Info();
            info.setTitle(title);
            vertex.info=info;
            
            List<Option> options = new ArrayList<Option>();
            vertex.options = options;
           
            if (i==0) {
                vertex.isTopVertex = 1;
            }
           
            vertexMap.put(id, vertex);
            
            UserObject uo = new UserObject();
            uo.setId(id);           
            MxObject mxObject = new MxObject(MxObjectType.USER_OBJECT, uo);
            objectMap.put(id, mxObject);
        }
        
        LOG.info("initialMessages:"+initialMessages.toString()); 
        int initMsgSize = initialMessages.length();
        for (int i=0;i<initMsgSize;i++) {
            JSONObject cell = (JSONObject)initialMessages.get(i);
            String id =cell.getString("@id");
            String parent = cell.getJSONObject("mxCell").getString("@parent");
            String text = cell.getJSONObject("mxCell").getJSONObject("div").getString("span");
            
            Vertex vertex = vertexMap.get(parent);
            vertex.info.setText(text);
            
            InitialMessage im = new InitialMessage();
            im.setId(id);           
            MxObject mxObject = new MxObject(MxObjectType.INITIAL_MESSAGE, im);
            objectMap.put(id, mxObject);
        }
        
        LOG.info("Triggers:"+triggers.toString());                        
        int triggerSize = triggers.length();
        for (int i=0;i<triggerSize;i++) {
            JSONObject cell = (JSONObject)triggers.get(i);
            String id = cell.getString("@id");
            String parent = cell.getJSONObject("mxCell").getString("@parent");
            String text = cell.getJSONObject("mxCell").getJSONObject("div").getJSONObject("button").getString("#text");
            LOG.info("Triggers["+i+"] -> id="+id+" Location:"+parent);
            Option option = new Option();
            option.setId(id);
            option.setText(text);
            
            Step step = stepMap.get(id);
            MxObject mx = objectMap.get(step.getTargetId());
            step.setTargetType(ConvertObjectType(mx.getObjectType()));
            option.setStep(step);
            stepMap.remove(id);
            
            Vertex vertex = vertexMap.get(parent);
            List<Option> vertexOption = vertex.options;
            vertexOption.add(option); 
            
            Trigger t = new Trigger();
            t.setId(id);           
            MxObject mxObject = new MxObject(MxObjectType.TRIGGER, t);
            objectMap.put(id, mxObject);
        }
        
        // Getting an iterator 
        Iterator stepIterator = stepMap.entrySet().iterator(); 
        while (stepIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)stepIterator.next(); 
            Step v = ((Step)mapElement.getValue()); 
            String k = ((String)mapElement.getKey()); 
            //find in objectmap
            LOG.info("Step sourceId:"+v.getSourceId());
            MxObject mxobject = objectMap.get(v.getSourceId());
            if (mxobject.getObjectType()==MxObjectType.ANCHOR) {
                Anchor a = (Anchor)mxobject.getObject();
                String vertexId = a.getParent();
                Vertex vertex = vertexMap.get(vertexId);
                v.setTargetType(VertexTargetType.FLOW);
                vertex.step = v;
                vertexMap.put(vertexId, vertex);
            }
        }
            
        Vertex[] allVertex = new Vertex[vertexMap.size()];         
        // Getting an iterator 
        Iterator hmIterator = vertexMap.entrySet().iterator(); 
        int x=0;
        while (hmIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
            Vertex v = ((Vertex)mapElement.getValue()); 
            allVertex[x]=v; 
            x++;
        }
        
        // Getting an iterator 
        Iterator idIterator = objectMap.entrySet().iterator(); 
        int c=0;
        while (idIterator.hasNext()) { 
            Map.Entry mapElement = (Map.Entry)idIterator.next(); 
            MxObject v = ((MxObject)mapElement.getValue());
            String k = (String)mapElement.getKey();
            LOG.info("objectIdMap["+c+"] key="+k+" value="+v.getObjectType());
            c++;
        }
        return allVertex;
    }
    
    
    private static JSONObject GetCorrectObjectFromDiv(JSONArray jsonArray, String targetClassName) {
        for (int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            String className = jsonObject.getString("@class");
            if (className.equals(targetClassName)) {
                return jsonObject;
            }
        }
        return null;
    }
    
    private static JSONArray extractJSONArray(JSONObject jsondata, String key) {
        if (!jsondata.isNull(key)) {
            Object item = jsondata.get(key); 

            // `instanceof` tells us whether the object can be cast to a specific type
            if (item instanceof JSONArray)
            {
                // it's an array
                return (JSONArray) item;
            }
            else
            {
                // if you know it's either an array or an object, then it's an object
                JSONObject jsonObject = (JSONObject) item;
                // conver to json array
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                return jsonArray;
            }
        } else {
            return null;
        }
    }
    
    private static VertexTargetType ConvertObjectType(MxObjectType objectType) {
        if (objectType==MxObjectType.USER_OBJECT) {
            return VertexTargetType.VERTEX;
        } else {
            return VertexTargetType.FLOW;
        }
    } 
    
    private static String extractStringFromJson(JSONObject jsonObject, String key) {
        try {
           String value = jsonObject.getString(key);
           return value;
        } catch (Exception ex) {
           return "";
        }
    }
}
