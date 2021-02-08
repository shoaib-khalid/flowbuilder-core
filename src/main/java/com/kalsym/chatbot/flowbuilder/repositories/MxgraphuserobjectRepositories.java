/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphuserobject;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author user
 */
public interface MxgraphuserobjectRepositories extends MongoRepository<Mxgraphuserobject, String> {

     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0}")
    public List <Mxgraphuserobject> getMxByFlowId(
            @Param("flowId") String flowId
    );
    
    
     /**
     * Delete  by flowId
     * @param flowId
     * @return Mxgraphuserobject object
     */
    @Query(value="{'flowId' : ?0}", delete = true)
    public List <Mxgraphuserobject> deleteUserObjectByFlowId(String flowId);
    
      /**
     * Finds mxgraph by flowId
     * @param flowId
     * @param mxId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0, 'mxId' : ?1}")
    public Mxgraphuserobject findByMxId(
            @Param("flowId") String flowId,
            @Param("mxId") String mxId
    );
}
