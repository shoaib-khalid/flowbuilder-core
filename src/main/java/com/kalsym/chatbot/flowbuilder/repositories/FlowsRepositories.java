/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.models.daos.Flow;
import com.kalsym.chatbot.flowbuilder.mxmodel.daos.Mxgraphdatavariable;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author user
 */
public interface FlowsRepositories extends MongoRepository<Flow, String> {

    /**
     * Finds Flow by flowId
     * @param botId
     * @param topVertexId
     * @return  complete mxgraph object
     */
    @Query("{'botId' : ?0, 'topVertexId' : ?1}")
    public List <Flow> getByBotIdAndTopVertexId(
            @Param("botId") String botId,
            @Param("topVertexId") String topVertexId
    );
    
    
    /**
     * Finds mxgraph by flowId
     * @param botId
     * @return  complete mxgraph object
     */
    @Query("{'botId' : ?0}")
    public List <Flow> getByBotId(
            @Param("botId") String botId
    );
}
