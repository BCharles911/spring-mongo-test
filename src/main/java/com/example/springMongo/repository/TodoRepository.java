package com.example.springMongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.springMongo.model.TodoDTO;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.mongodb.repository.Query;
@Repository
public interface TodoRepository extends MongoRepository<TodoDTO, String> {
	
	@Query("{'todo': ?0}")
	Optional<TodoDTO> findByTodo(String todo);
}
