
package com.benjacortes.backendsystem.service;

import com.benjacortes.backendsystem.data.ToDo;
import com.benjacortes.backendsystem.repository.ToDoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoService {
    
    @Autowired
    private ToDoRepository toDoRepository;

    public Iterable<ToDo> findAll() {
        return toDoRepository.findAll();
    }

    public ToDo findById(Integer id) {
        return toDoRepository.findById(id).orElse(null);
    }
}
