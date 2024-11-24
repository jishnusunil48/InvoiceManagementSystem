package com.example.IMS.enums;

public enum InvoiceStatus {
    PENDING("PENDING"),
    PAID("PAID"),
    VOID("VOID");

    private final String value;

    InvoiceStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

