/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import com.kalsym.chatbot.flowbuilder.mxmodel.Mxgraphmodel;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author user
 */
public interface VertexsRepositories extends MongoRepository<Vertex, String> {

     /**
     * Delete  by flowId
     * @param flowId
     * @return vertex object
     */
    @Query(value="{'flowId' : ?0}", delete = true)
    public List <Vertex> deleteVertexByFlowId(String flowId);
}
