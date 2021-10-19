
package com.benjacortes.backendsystem.service;

import java.util.List;

import com.benjacortes.backendsystem.data.ToDo;
import com.benjacortes.backendsystem.repository.ToDoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToDoService {
    
    @Autowired
    private ToDoRepository toDoRepository;

    public List<ToDo> findAll() {
        return toDoRepository.findAll();
    }

    public ToDo findById(String id) {
        return toDoRepository.findById(id).orElse(null);
    }

    public ToDo save(ToDo toDo) {
        return toDoRepository.save(toDo);
    }

    public String deleteById(String id) {
        toDoRepository.deleteById(id);
        return String.format("Object with ID %s Deleted", id);
    }
}
