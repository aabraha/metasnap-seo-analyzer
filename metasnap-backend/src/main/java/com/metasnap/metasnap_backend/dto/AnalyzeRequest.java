package com.metasnap.metasnap_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AnalyzeRequest {
    @NotBlank(message = "URL is required")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
} 