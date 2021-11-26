package com.benjacortes.backendsystem.repository;

import com.benjacortes.backendsystem.data.ToDo;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface ToDoRepository extends MongoRepository<ToDo, String> {}
