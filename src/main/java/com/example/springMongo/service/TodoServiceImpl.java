package com.example.springMongo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springMongo.exception.TodoCollectionException;
import com.example.springMongo.model.TodoDTO;
import com.example.springMongo.repository.TodoRepository;


@Service
public class TodoServiceImpl implements TodoService {
	
	@Autowired
	private TodoRepository todoRepository;
	
	@Override
	public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException {
		Optional<TodoDTO> todoOptional =todoRepository.findByTodo(todo.getTodo());
		if(todoOptional.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.todoAlreadyExsists());
			
		}else {
			todo.setCreatedAt(new Date(System.currentTimeMillis()));
			todoRepository.save(todo);
		}
	}
	
	@Override
	public List<TodoDTO> getAllTodos(){
		List<TodoDTO> todos = todoRepository.findAll();
		if(todos.size() > 0) {
			return todos;
		}else {
			return new ArrayList<TodoDTO>();
		}
	}
	
	@Override
	public TodoDTO getSingleTodo(String id) throws TodoCollectionException {
		Optional<TodoDTO> todo = todoRepository.findById(id);
		
		if(!todo.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
			
		} else {
			return todo.get();
		}
	}

	@Override
	public void updateTodo(String id, TodoDTO todo) throws TodoCollectionException {
		Optional<TodoDTO> todoWithId = todoRepository.findById(id);
		Optional<TodoDTO> todoWithSameName = todoRepository.findByTodo(todo.getTodo());
		if(todoWithId.isPresent()) {
			if(todoWithSameName.isPresent() &&  todoWithSameName.get().getId().equals(id)) {
				throw new TodoCollectionException(TodoCollectionException.todoAlreadyExsists());
			}
			TodoDTO todoToUpdate = 	todoWithId.get();
			todoToUpdate.setTodo(todo.getTodo());
			todoToUpdate.setDescription(todo.getDescription());
			todoToUpdate.setCompleted(todo.getCompleted());
			todo.setUpdatedAt(new Date(System.currentTimeMillis()));
			todoRepository.save(todoToUpdate);		
		}else {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}
		
	}

	@Override
	public void deleteTodoById(String id) throws TodoCollectionException {
		Optional<TodoDTO> todo = todoRepository.findById(id);
		if(!todo.isPresent()) {
			throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
		}else {
			todoRepository.deleteById(id);
		}
		
	}


}
