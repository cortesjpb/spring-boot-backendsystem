package com.benjacortes.backendsystem.data;

public enum PaymentStatus {
    CANCELED("CANCELED"),
    ACCEPTED("ACCEPTED"),
    PENDING("PENDING"),
    SENT("SENT"),
    PROCESSING("PROCESSING");

    private String value;

    private PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
