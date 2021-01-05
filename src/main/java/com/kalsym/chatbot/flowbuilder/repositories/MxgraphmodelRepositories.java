/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.mxmodel.Mxgraphmodel;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author user
 */
public interface MxgraphmodelRepositories extends MongoRepository<Mxgraphmodel, String> {

     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0}")
    public Mxgraphmodel getMxByFlowId(
            @Param("flowId") String flowId
    );
    
    
     /**
     * Delete  by flowId
     * @param flowId
     * @return Mxgraphmodel object
     */
    @Query(value="{'flowId' : ?0}", delete = true)
    public List <Mxgraphmodel> deleteMxgraphByFlowId(String flowId);
    
}
