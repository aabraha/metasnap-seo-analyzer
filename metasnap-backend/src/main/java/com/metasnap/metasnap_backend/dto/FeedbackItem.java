package com.metasnap.metasnap_backend.dto;

public class FeedbackItem {
    private String type; // good, warning, missing
    private String message;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
} 