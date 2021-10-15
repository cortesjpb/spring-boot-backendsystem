package com.benjacortes.backendsystem.repository;

import com.benjacortes.backendsystem.data.ToDo;

import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository extends CrudRepository<ToDo, Integer> {}
