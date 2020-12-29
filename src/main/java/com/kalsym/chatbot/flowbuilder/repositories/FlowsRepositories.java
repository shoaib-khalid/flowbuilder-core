/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.models.daos.Flow;
import java.util.List;

/**
 *
 * @author user
 */
public interface FlowsRepositories extends MongoRepository<Flow, String> {

}
