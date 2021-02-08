/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.utils;

import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphmxcell;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphuserobject;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphtrigger;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphdatavariable;

/**
 *
 * @author user
 */
public class VertexDecoderResult {
    public Vertex[] vertexList;
    public Mxgraphuserobject[] userObjectList;
    public Mxgraphmxcell[] mxCellList;
    public Mxgraphtrigger[] triggerList;  
    public Mxgraphdatavariable[] dataVariableList;
}
