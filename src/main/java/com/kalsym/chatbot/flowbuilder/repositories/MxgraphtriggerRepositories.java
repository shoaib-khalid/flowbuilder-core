/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphtrigger;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author user
 */
public interface MxgraphtriggerRepositories extends MongoRepository<Mxgraphtrigger, String> {

     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0}")
    public List <Mxgraphtrigger> getMxByFlowId(
            @Param("flowId") String flowId
    );
    
    
     /**
     * Delete  by flowId
     * @param flowId
     * @return Mxgraphtrigger object
     */
    @Query(value="{'flowId' : ?0}", delete = true)
    public List <Mxgraphtrigger> deleteTriggerByFlowId(String flowId);
    
     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @param mxId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0, 'mxId' : ?1}")
    public Mxgraphtrigger findByMxId(
            @Param("flowId") String flowId,
            @Param("mxId") String mxId
    );
    
    
     /**
     * Finds mxtrigger by flowId & userObjectMxId
     * @param flowId
     * @param userObjectMxId
     * @return  complete mxtrigger object
     */
   @Query("{'flowId' : ?0, 'triggers.mxCell.@parent' : ?1}")
   public List<Mxgraphtrigger> findByUserObjectMxId(
            @Param("flowId") String flowId,
            @Param("userObjectMxId") String userObjectMxId
   );
}
