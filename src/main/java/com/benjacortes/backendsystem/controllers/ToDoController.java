package com.benjacortes.backendsystem.controllers;

import com.benjacortes.backendsystem.data.ToDo;
import com.benjacortes.backendsystem.service.ToDoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todo")
public class ToDoController {
    
    @Autowired
    private ToDoService toDoService;

    @GetMapping
    public Iterable<ToDo> findAll() {
        return toDoService.findAll();
    }

    @GetMapping("/{id}")
    public ToDo findById(@PathVariable Integer id){
        return toDoService.findById(id);
    }

}
