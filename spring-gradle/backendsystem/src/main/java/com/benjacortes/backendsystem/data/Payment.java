package com.benjacortes.backendsystem.data;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Payment {
    
    @Id
    private String id;

    private String card;

    private String description;
    
    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;

    private double total;

    private String paymentStatus;

    public Payment() {}

    public Payment(String card, String description, double total) {
        this.card = card;
        this.description = description;
        this.total = total;
        this.paymentStatus = PaymentStatus.PENDING.getValue();
    }

    public String getId() {
        return id;
    }

    public String getCard() {
        return card;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getDescription() {
        return description;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public double getTotal() {
        return total;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Payment ID: " + this.id + " - Descripcion: " + this.description + " - Total: " + this.total;
    }

}

