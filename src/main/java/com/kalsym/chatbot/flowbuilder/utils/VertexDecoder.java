/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import com.kalsym.chatbot.flowbuilder.models.enums.VertexTargetType;
import com.kalsym.chatbot.flowbuilder.submodels.Option;
import com.kalsym.chatbot.flowbuilder.submodels.Step;
import com.kalsym.chatbot.flowbuilder.submodels.Info;
import com.kalsym.chatbot.flowbuilder.submodels.DataVariable;
import com.kalsym.chatbot.flowbuilder.submodels.Handover;
import com.kalsym.chatbot.flowbuilder.submodels.Button;
import com.kalsym.chatbot.flowbuilder.mxmodel.*;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.gson.*;
import com.kalsym.chatbot.flowbuilder.models.enums.VertexActionType;
import com.kalsym.chatbot.flowbuilder.models.enums.VertexType;
import com.kalsym.chatbot.flowbuilder.submodels.Action;
import com.kalsym.chatbot.flowbuilder.submodels.ExternalRequest;
import com.kalsym.chatbot.flowbuilder.submodels.ExternalRequestBody;
import com.kalsym.chatbot.flowbuilder.submodels.ExternalRequestReponse;
import java.util.logging.Level;

/**
 *
 * @author user
 */
public class VertexDecoder {

    private static final Logger LOG = LoggerFactory.getLogger("application");

    public static VertexDecoderResult Decode(JsonObject rawjson) throws JsonProcessingException {

        Map<String, MxDataVariable> dataVariableMap = new HashMap<>();
        Map<String, Button> buttonMap = new HashMap<>();
        JsonArray dataVariableArray = rawjson.get("data").getAsJsonArray();
        for (int i = 0; i < dataVariableArray.size(); i++) {
            JsonObject dataVariable = dataVariableArray.get(i).getAsJsonObject();
            LOG.info("dataVariable[" + i + "]:" + dataVariable.toString());
            String type = dataVariable.get("type").getAsString();
            String vertexId = dataVariable.get("vertexId").getAsString();
            MxDataVariable dataVar = new MxDataVariable();
            if (type.equals("MENU_MESSAGE")) {
                dataVar.setType(VertexType.MENU_MESSAGE);
            } else if (type.equals("ACTION")) {
                dataVar.setType(VertexType.ACTION);
            } else if (type.equals("CONDITION")) {
                dataVar.setType(VertexType.CONDITION);
            } else if (type.equals("TEXT_MESSAGE")) {
                dataVar.setType(VertexType.TEXT_MESSAGE);
            } else if (type.equals("HANDOVER")) {
                dataVar.setType(VertexType.HANDOVER);
            } else if (type.equals("IMMEDIATE_TEXT_MESSAGE")) {
                dataVar.setType(VertexType.IMMEDIATE_TEXT_MESSAGE);
            }
            dataVar.setVertexId(vertexId);

            JsonArray dataArray = dataVariable.get("dataVariables").getAsJsonArray();
            List<DataVariable> dataVariableList = new ArrayList<>();
            for (int x = 0; x < dataArray.size(); x++) {
                JsonObject dataObject = dataArray.get(x).getAsJsonObject();
                String dataid = dataObject.get("id").getAsString();
                String data = dataObject.get("dataVariable").getAsString();
                String path = dataObject.get("path").getAsString();
                String optional = dataObject.get("optional").getAsString();
                DataVariable dataVariableObject = new DataVariable();
                dataVariableObject.setId(dataid);
                dataVariableObject.setDataVariable(data);
                dataVariableObject.setPath(path);
                dataVariableObject.setOptional(optional);
                dataVariableList.add(dataVariableObject);
            }
            dataVar.setDataList(dataVariableList);

            if (dataVariable.get("buttons") != null) {
                JsonArray buttonArray = dataVariable.get("buttons").getAsJsonArray();
                List<Button> buttonList = new ArrayList<>();
                for (int x = 0; x < buttonArray.size(); x++) {
                    JsonObject bObject = buttonArray.get(x).getAsJsonObject();
                    LOG.info("dataVariableArray[" + i + "] Button[" + x + "]=" + bObject.toString());
                    String btnTitle = bObject.get("btnTitle").getAsString();
                    String btnValue = bObject.get("btnValue").getAsString();
                    Button buttonObject = new Button();
                    buttonObject.setIndex(x);
                    buttonObject.setBtnTitle(btnTitle);
                    buttonObject.setBtnValue(btnValue);
                    buttonList.add(buttonObject);
                    String buttonKey = vertexId + "," + x;
                    buttonMap.put(buttonKey, buttonObject);
                    LOG.info("dataVariableArray[" + i + "] Stored button into hashmap with key:" + buttonKey);
                }
                dataVar.setButtons(buttonList);
            }

            if (dataVariable.get("actions") != null) {
                JsonArray actionArray = dataVariable.get("actions").getAsJsonArray();

                List<Action> actions = new ArrayList<>();
                for (int x = 0; x < actionArray.size(); x++) {
                    JsonObject bObject = actionArray.get(x).getAsJsonObject();
                    LOG.info("dataVariableArray[" + i + "] bObject:" + bObject.toString());

                    Action action = new Action();
                    if (bObject.has("externalRequest")) {

//                        ObjectMapper objectMapper = new ObjectMapper();
//                        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);
//
//                        ExternalRequest er = null;
//                        try {
//                            JsonObject erJob = bObject.getAsJsonObject("externalRequest");
//                            er = objectMapper.readValue(.toString(), ExternalRequest.class);
//                        } catch (JsonProcessingException ex) {
//                            java.util.logging.Logger.getLogger(VertexDecoder.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        LOG.info("dataVariableArray[" + i + "] er:" + er.toString());
                        ObjectMapper objectMapper = new ObjectMapper();
                        Gson gson = new Gson();
                        LOG.info("dataVariableArray[" + i + "] bObject:" + bObject.get("externalRequest").toString());
                        action.setType(VertexActionType.EXTERNAL_REQUEST);
                        JsonObject erJob = bObject.getAsJsonObject("externalRequest");
                        ExternalRequest er = new ExternalRequest();

                        //HashMap<String, String> headers = objectMapper.readValue(erJob.getAsJsonArray("headers").toString(), HashMap.class);
                        //LOG.info("dataVariableArray[" + i + "] headers:" + erJob.getAsJsonArray("headers").toString());
                        HashMap<String, String> headers = gson.fromJson(erJob.get("headers"), HashMap.class);

                        er.setHeaders(headers);
                        er.setUrl(erJob.get("url").toString());

                        ExternalRequestBody erb = gson.fromJson(erJob.get("body"), ExternalRequestBody.class);
                        ExternalRequestReponse err = gson.fromJson(erJob.get("response"), ExternalRequestReponse.class);

                        er.setBody(erb);
                        er.setResponse(err);
                        //er.setErrorStep(new Gson().fromJson(erJob.getAsJsonObject("errorStep"), Step.class));
                        er.setBody(new Gson().fromJson(erJob.getAsJsonObject("body"), ExternalRequestBody.class));
                        action.setExternalRequest(er);
                    }

                    actions.add(action);
                    
                }
                dataVar.setActions(actions);

            }

            LOG.info("dataVariableArray[" + i + "] -> type=" + type + " vertexId=" + vertexId);
            dataVariableMap.put(vertexId, dataVar);
        }

        JsonObject json = rawjson.get("mxGraphModel").getAsJsonObject();

        VertexDecoderResult result = new VertexDecoderResult();

        JsonObject jsondata = json.getAsJsonObject("root");
        JsonArray mxcell = extractJSONArray(jsondata, "mxCell");
        JsonArray userobject = extractJSONArray(jsondata, "UserObject");
        JsonArray triggers = extractJSONArray(jsondata, "triggers");
        JsonArray initialMessages = extractJSONArray(jsondata, "InitialMessage");

        Map<String, MxObject> objectMap = new HashMap<>();

        //extract Vertex Step from edge in mxcell
        Map<String, Step> stepMap = new HashMap<>();
        if (mxcell != null) {
            LOG.info("mxcell:" + mxcell.toString());
            int mxcellSize = mxcell.size();
            for (int i = 0; i < mxcellSize; i++) {
                JsonObject cell = (JsonObject) mxcell.get(i);
                String id = cell.get("@id").getAsString();
                LOG.info("Cell[" + i + "] -> id=" + id);
                String isEdge = extractStringFromJson(cell, "@edge");
                if (isEdge.equals("1")) {
                    String parentId = cell.get("@parent").getAsString();
                    String sourceTriggerId = cell.get("@source").getAsString();
                    String targetVertexId = cell.get("@target").getAsString();
                    LOG.info("Edge[" + i + "] -> sourceTriggerId=" + sourceTriggerId + " targetVertexId=" + targetVertexId);
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
                    String isVertex = extractStringFromJson(cell, "@vertex");
                    if (isVertex.equalsIgnoreCase("1")) {
                        String parentId = cell.get("@parent").getAsString();
                        String value = cell.get("@value").getAsString();
                        Anchor anchor = new Anchor();
                        anchor.setId(id);
                        anchor.setParent(parentId);
                        anchor.setValue(value);
                        MxObject mxObject = new MxObject(MxObjectType.ANCHOR, anchor);
                        objectMap.put(id, mxObject);
                    }
                }
            }
        }

        //extract Vertex from UserObject            
        Map<String, Vertex> vertexMap = new HashMap<>();
        if (userobject != null) {
            LOG.info("userobject:" + userobject.toString());
            int userSize = userobject.size();
            for (int i = 0; i < userSize; i++) {
                JsonObject cell = (JsonObject) userobject.get(i);
                String id = cell.get("@id").getAsString();
                LOG.info("UserObject[" + i + "] -> id=" + id);

                JsonObject t1 = cell.get("mxCell").getAsJsonObject().get("div").getAsJsonObject().get("div").getAsJsonObject();
                JsonArray a1 = t1.get("div").getAsJsonArray();

                //get card object
                JsonObject t2 = GetCorrectObjectFromDiv(a1, "card");
                LOG.info("UserObject[" + i + "] -> t2=" + t2.toString());
                JsonArray a2 = t2.get("div").getAsJsonArray();

                //extract title from card-header
                String title = "";
                JsonObject cardHeader = GetCorrectObjectFromDiv(a2, "card-header");
                if (cardHeader != null) {
                    LOG.info("UserObject[" + i + "] -> cardHeader=" + cardHeader.toString());
                    try {
                        title = cardHeader.get("div").getAsJsonObject().get("h4").getAsJsonObject().get("#text").getAsString();
                        LOG.info("UserObject[" + i + "] -> title=" + title);
                    } catch (Exception ex) {
                    }
                }

                //extract text from card-body
                String text = "";
                JsonObject cardBody = GetCorrectObjectFromDiv(a2, "card-body");
                if (cardBody != null) {
                    LOG.info("UserObject[" + i + "] -> cardBody=" + cardBody.toString());
                    try {
                        text = cardBody.get("span").getAsJsonObject().get("#text").getAsString();
                        LOG.info("UserObject[" + i + "] -> text=" + text);
                    } catch (Exception ex) {
                    }
                }

                Vertex vertex = new Vertex();
                vertex.mxId = id;
                Info info = new Info();
                info.setTitle(title);
                info.setText(text);

                //get vertex type from dataVariable map
                MxDataVariable dataVar = dataVariableMap.get(vertex.mxId);
                if (dataVar != null) {
                    //data variable is different depend on vertex type 
                    info.setType(dataVar.getType());
                    if (dataVar.getType() == VertexType.MENU_MESSAGE
                            || dataVar.getType() == VertexType.TEXT_MESSAGE
                            || dataVar.getType() == VertexType.IMMEDIATE_TEXT_MESSAGE) {
                        //menu message, only 1 variable
                        if (dataVar.getDataList().size() > 0) {
                            vertex.dataVariable = dataVar.getDataList().get(0).getDataVariable();
                        }
                    } else if (dataVar.getType() == VertexType.ACTION) {
                        //vertex type is action
                    } else if (dataVar.getType() == VertexType.HANDOVER) {
                        //vertex type is handover
                        //only 1 datavariable
                        if (dataVar.getDataList().size() > 0) {
                            vertex.dataVariable = dataVar.getDataList().get(0).getDataVariable();
                        }
                        //TODO:extract handover connect & forward msg
                        Handover handover = new Handover();
                        handover.setConnectMessage("");
                        handover.setForwardMessage("");
                        vertex.handover = handover;
                    }
                    //set vertex type
                    info.setType(dataVar.getType());
                }

                vertex.info = info;

                List<Option> options = new ArrayList<>();
                vertex.options = options;

                if (i == 0) {
                    vertex.isTopVertex = 1;
                }

                //save vertex in VertexMap
                LOG.info("UserObject[" + i + "] Save vertex into vertexMap:" + id);
                vertexMap.put(id, vertex);

                UserObject uo = new UserObject();
                uo.setId(id);
                MxObject mxObject = new MxObject(MxObjectType.USER_OBJECT, uo);
                objectMap.put(id, mxObject);
            }
        }

        //extract Vertex Info from initialMessages
        /*LOG.info("initialMessages:"+initialMessages.toString()); 
        int initMsgSize = initialMessages.length();
        for (int i=0;i<initMsgSize;i++) {
            JSONObject cell = (JSONObject)initialMessages.get(i);
            String id =cell.getString("@id");
            String parent = cell.getJSONObject("mxCell").getString("@parent");
            String text = cell.getJSONObject("mxCell").getJSONObject("div").getString("span");
            
            //set Vertex Info
            Vertex vertex = vertexMap.get(parent);
            vertex.info.setText(text);
            
            InitialMessage im = new InitialMessage();
            im.setId(id);           
            MxObject mxObject = new MxObject(MxObjectType.INITIAL_MESSAGE, im);
            objectMap.put(id, mxObject);
        }*/
        //extract Vertex Option from Triggers                    
        if (triggers != null) {
            LOG.info("Triggers:" + triggers.toString());
            int triggerSize = triggers.size();
            for (int i = 0; i < triggerSize; i++) {
                JsonObject cell = (JsonObject) triggers.get(i);
                String id = cell.get("@id").getAsString();
                String parent = cell.get("mxCell").getAsJsonObject().get("@parent").getAsString();
                String text = cell.get("mxCell").getAsJsonObject().get("div").getAsJsonObject().get("div").getAsJsonObject().get("button").getAsJsonObject().get("#text").getAsString();
                //String value = 
                LOG.info("Triggers[" + i + "] -> id=" + id + " Location:" + parent);
                Option option = new Option();
                option.setId(id);
                option.setText(text);

                //get option value from button value
                String buttonKey = parent + "," + i;
                LOG.info("Triggers[" + i + "] -> Retrieve value from buttonMap with key:" + buttonKey);
                Button button = buttonMap.get(buttonKey);
                if (button != null) {
                    LOG.info("Triggers[" + i + "] -> Button found index:" + button.getIndex() + " title:" + button.getBtnTitle() + " value:" + button.getBtnValue());
                    option.setValue(button.getBtnValue());
                }

                Step step = stepMap.get(id);
                if (step != null) {
                    MxObject mx = objectMap.get(step.getTargetId());
                    step.setTargetType(ConvertObjectType(mx.getObjectType()));
                    option.setStep(step);
                    stepMap.remove(id);

                    //set Vertex Options with step
                    Vertex vertex = vertexMap.get(parent);
                    List<Option> vertexOption = vertex.options;
                    vertexOption.add(option);
                    LOG.info("Triggers[" + i + "] -> id=" + id + " is an edge");

                } else {
                    //set Vertex Option without step
                    Vertex vertex = vertexMap.get(parent);
                    List<Option> vertexOption = vertex.options;
                    vertexOption.add(option);
                    LOG.info("Triggers[" + i + "] -> id=" + id + " is not an edge");
                }

                Trigger t = new Trigger();
                t.setId(id);
                MxObject mxObject = new MxObject(MxObjectType.TRIGGER, t);
                objectMap.put(id, mxObject);
            }
        }

        //set Vertex Step  using StepMap
        Iterator stepIterator = stepMap.entrySet().iterator();
        int s = 0;
        while (stepIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) stepIterator.next();
            Step v = ((Step) mapElement.getValue());
            String k = ((String) mapElement.getKey());
            //find sourceId in objectmap to get object
            LOG.info("Step[" + s + "] sourceId:" + v.getSourceId() + " targetId:" + v.getTargetId());
            MxObject mxobject = objectMap.get(v.getSourceId());
            LOG.info("Step[" + s + "] source Object Type:" + mxobject.getObjectType());
            if (mxobject.getObjectType() == MxObjectType.ANCHOR) {
                Anchor a = (Anchor) mxobject.getObject();
                String vertexId = a.getParent();
                Vertex vertex = vertexMap.get(vertexId);
                v.setTargetType(VertexTargetType.FLOW);
                vertex.step = v;
                LOG.info("Step[" + s + "] vertex id:" + vertexId);
                vertexMap.put(vertexId, vertex);
            } else if (mxobject.getObjectType() == MxObjectType.USER_OBJECT) {
                Vertex vertex = vertexMap.get(v.getSourceId());
                v.setTargetType(VertexTargetType.VERTEX);
                vertex.step = v;
                LOG.info("Step[" + s + "] vertex mxId:" + vertex.id);
                vertexMap.put(vertex.mxId, vertex);
            }
            s++;
        }

        //convert vertex to array to return to caller
        Vertex[] allVertex = new Vertex[vertexMap.size()];
        Iterator hmIterator = vertexMap.entrySet().iterator();
        int x = 0;
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) hmIterator.next();
            Vertex v = ((Vertex) mapElement.getValue());
            allVertex[x] = v;
            x++;
        }

        //print object map with its ID
        Iterator idIterator = objectMap.entrySet().iterator();
        int c = 0;
        while (idIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) idIterator.next();
            MxObject v = ((MxObject) mapElement.getValue());
            String k = (String) mapElement.getKey();
            LOG.info("objectIdMap[" + c + "] key=" + k + " value=" + v.getObjectType());
            c++;
        }
        result.vertexList = allVertex;

        //set mxcell to result
        if (mxcell != null) {
            Mxgraphmxcell[] mxcellList = new Mxgraphmxcell[mxcell.size()];
            for (int i = 0; i < mxcell.size(); i++) {
                Mxgraphmxcell mxgraphmxcell = new Mxgraphmxcell();
                Object dataObject = BasicDBObject.parse(mxcell.get(i).toString());
                mxgraphmxcell.mxCell = (DBObject) dataObject;
                mxgraphmxcell.mxId = mxcell.get(i).getAsJsonObject().get("@id").getAsString();
                mxcellList[i] = mxgraphmxcell;
            }
            result.mxCellList = mxcellList;
        }

        //set userobject to result
        if (userobject != null) {
            Mxgraphuserobject[] userObjectList = new Mxgraphuserobject[userobject.size()];
            for (int i = 0; i < userobject.size(); i++) {
                Mxgraphuserobject mxgraphuserobject = new Mxgraphuserobject();
                Object dataObject = BasicDBObject.parse(userobject.get(i).toString());
                mxgraphuserobject.userObject = (DBObject) dataObject;
                mxgraphuserobject.mxId = userobject.get(i).getAsJsonObject().get("@id").getAsString();
                userObjectList[i] = mxgraphuserobject;
            }
            result.userObjectList = userObjectList;
        }

        //set trigger to result
        if (triggers != null) {
            Mxgraphtrigger[] triggerList = new Mxgraphtrigger[triggers.size()];
            for (int i = 0; i < triggers.size(); i++) {
                Mxgraphtrigger mxgraphtrigger = new Mxgraphtrigger();
                Object dataObject = BasicDBObject.parse(triggers.get(i).toString());
                mxgraphtrigger.triggers = (DBObject) dataObject;
                mxgraphtrigger.mxId = triggers.get(i).getAsJsonObject().get("@id").getAsString();
                triggerList[i] = mxgraphtrigger;
            }
            result.triggerList = triggerList;
        }

        //set data variable to result
        if (dataVariableArray.size() > 0) {
            Mxgraphdatavariable[] mxDataVariableList = new Mxgraphdatavariable[dataVariableArray.size()];
            for (int i = 0; i < dataVariableArray.size(); i++) {
                Object dataObject = BasicDBObject.parse(dataVariableArray.get(i).toString());
                Mxgraphdatavariable mxDataVariable = new Mxgraphdatavariable();
                String vertexId = dataVariableArray.get(i).getAsJsonObject().get("vertexId").getAsString();
                mxDataVariable.vertexId = vertexId;
                mxDataVariable.dataVariable = (DBObject) dataObject;
                mxDataVariableList[i] = mxDataVariable;
            }
            result.dataVariableList = mxDataVariableList;
        }

        return result;
    }

    private static JsonObject GetCorrectObjectFromDiv(JsonArray jsonArray, String targetClassName) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            String className = jsonObject.get("@class").getAsString();
            if (className.contains(targetClassName)) {
                return jsonObject;
            }
        }
        return null;
    }

    private static JsonArray extractJSONArray(JsonObject jsondata, String key) {
        if (jsondata.get(key) != null) {
            Object item = jsondata.get(key);

            // `instanceof` tells us whether the object can be cast to a specific type
            if (item instanceof JsonArray) {
                // it's an array
                return (JsonArray) item;
            } else {
                // if you know it's either an array or an object, then it's an object
                JsonObject jsonObject = (JsonObject) item;
                // conver to json array
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(jsonObject);
                return jsonArray;
            }
        } else {
            return null;
        }
    }

    private static VertexTargetType ConvertObjectType(MxObjectType objectType) {
        if (objectType == MxObjectType.USER_OBJECT) {
            return VertexTargetType.VERTEX;
        } else {
            return VertexTargetType.FLOW;
        }
    }

    private static String extractStringFromJson(JsonObject jsonObject, String key) {
        try {
            String value = jsonObject.get(key).getAsString();
            return value;
        } catch (Exception ex) {
            return "";
        }
    }
}
