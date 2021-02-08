/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphdatavariable;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author user
 */
public interface MxgraphdatavariableRepositories extends MongoRepository<Mxgraphdatavariable, String> {

     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0}")
    public List <Mxgraphdatavariable> getMxByFlowId(
            @Param("flowId") String flowId
    );
    
    
      /**
     * Finds mxgraph by flowId
     * @param flowId
     * @param vertexId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0, 'vertexId' : ?1}")
    public Mxgraphdatavariable getMxByFlowIdAndVertexId(
            @Param("flowId") String flowId,
            @Param("vertexId") String vertexId
    );
    
    
     /**
     * Delete  by flowId
     * @param flowId
     * @return Mxgraphmodel object
     */
    @Query(value="{'flowId' : ?0}", delete = true)
    public List <Mxgraphdatavariable> deleteDataVariableByFlowId(String flowId);
    
    
     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @param mxId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0, 'mxId' : ?1}")
    public Mxgraphdatavariable findByMxId(
            @Param("flowId") String flowId,
            @Param("mxId") String mxId
    );
    
    
     /**
     * Finds mxgraph by flowId & userObjectMxId
     * @param flowId
     * @param userObjectMxId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0, $or: [ {'mxCell.@source':?1},{'mxCell.@target':?1}]}")
    public List<Mxgraphdatavariable> findByUserObjectMxId(
            @Param("flowId") String flowId,
            @Param("userObjectMxId") String userObjectMxId
    );
}
