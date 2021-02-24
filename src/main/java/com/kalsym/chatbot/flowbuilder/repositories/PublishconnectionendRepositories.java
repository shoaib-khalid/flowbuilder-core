/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Publishconnectionend;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author user
 */
public interface PublishconnectionendRepositories extends MongoRepository<Publishconnectionend, String> {

     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0}")
    public List <Publishconnectionend> getMxByFlowId(
            @Param("flowId") String flowId
    );
    
    
     /**
     * Delete  by flowId
     * @param flowId
     * @return Mxgraphtrigger object
     */
    @Query(value="{'flowId' : ?0}", delete = true)
    public List <Publishconnectionend> deleteConnectionEndByFlowId(String flowId);
    
     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @param mxId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0, 'mxId' : ?1}")
    public Publishconnectionend findByMxId(
            @Param("flowId") String flowId,
            @Param("mxId") String mxId
    );
    
    
     /**
     * Finds mxtrigger by flowId & userObjectMxId
     * @param flowId
     * @param userObjectMxId
     * @return  complete mxtrigger object
     */
   @Query("{'flowId' : ?0, 'conditions.mxCell.@parent' : ?1}")
   public List<Publishconnectionend> findByUserObjectMxId(
            @Param("flowId") String flowId,
            @Param("userObjectMxId") String userObjectMxId
   );
}
