package com.benjacortes.backendsystem.data;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class Payment {
    
    @Id
    private String id;

    private String card;

    private String description;
    
    @CreatedDate
    private Date timestmp;
    
    private float total;

    public Payment() {
    }

    public Payment(String card, String description, float total) {
        this.card = card;
        this.description = description;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public String getCard() {
        return card;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimestmp() {
        return timestmp;
    }

    public float getTotal() {
        return total;
    }

}
