package com.benjacortes.backendsystem.data;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ToDo {
    
    @Id
    private Integer id;

    private String title;

    private Boolean completed;

    public ToDo(String title, Boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    
}
