/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphmxcell;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author user
 */
public interface MxgraphmxcellRepositories extends MongoRepository<Mxgraphmxcell, String> {

     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0}")
    public List <Mxgraphmxcell> getMxByFlowId(
            @Param("flowId") String flowId
    );
    
    
     /**
     * Delete  by flowId
     * @param flowId
     * @return Mxgraphmodel object
     */
    @Query(value="{'flowId' : ?0}", delete = true)
    public List <Mxgraphmxcell> deleteMxcellByFlowId(String flowId);
    
    
     /**
     * Finds mxgraph by flowId
     * @param flowId
     * @param mxId
     * @return  complete mxgraph object
     */
   @Query("{'flowId' : ?0, 'mxId' : ?1}")
    public Mxgraphmxcell findByMxId(
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
    public List<Mxgraphmxcell> findByUserObjectMxId(
            @Param("flowId") String flowId,
            @Param("userObjectMxId") String userObjectMxId
    );
}
