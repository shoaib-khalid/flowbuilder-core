/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Publishuserobject;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author user
 */
public interface PublishuserobjectRepositories extends MongoRepository<Publishuserobject, String> {

     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0}")
    public List <Publishuserobject> getMxByFlowId(
            @Param("flowId") String flowId
    );
    
    
     /**
     * Delete  by flowId
     * @param flowId
     * @return Mxgraphuserobject object
     */
    @Query(value="{'flowId' : ?0}", delete = true)
    public List <Publishuserobject> deleteUserObjectByFlowId(String flowId);
    
      /**
     * Finds mxgraph by flowId
     * @param flowId
     * @param mxId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0, 'mxId' : ?1}")
    public Publishuserobject findByMxId(
            @Param("flowId") String flowId,
            @Param("mxId") String mxId
    );
}
