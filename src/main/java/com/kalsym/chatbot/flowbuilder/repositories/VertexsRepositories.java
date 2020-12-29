/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kalsym.chatbot.flowbuilder.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.kalsym.chatbot.flowbuilder.models.daos.Vertex;
import java.util.List;

/**
 *
 * @author user
 */
public interface VertexsRepositories extends MongoRepository<Vertex, String> {

}
