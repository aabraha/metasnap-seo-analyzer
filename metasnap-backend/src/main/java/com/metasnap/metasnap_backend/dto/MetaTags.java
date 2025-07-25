package com.metasnap.metasnap_backend.dto;

public class MetaTags {
    private String title;
    private String description;
    private String canonical;
    private String ogTitle;
    private String ogDescription;
    private String ogImage;
    private String twitterTitle;
    private String twitterDescription;
    private String twitterImage;
    private String robots;
    private String structuredData;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCanonical() { return canonical; }
    public void setCanonical(String canonical) { this.canonical = canonical; }
    public String getOgTitle() { return ogTitle; }
    public void setOgTitle(String ogTitle) { this.ogTitle = ogTitle; }
    public String getOgDescription() { return ogDescription; }
    public void setOgDescription(String ogDescription) { this.ogDescription = ogDescription; }
    public String getOgImage() { return ogImage; }
    public void setOgImage(String ogImage) { this.ogImage = ogImage; }
    public String getTwitterTitle() { return twitterTitle; }
    public void setTwitterTitle(String twitterTitle) { this.twitterTitle = twitterTitle; }
    public String getTwitterDescription() { return twitterDescription; }
    public void setTwitterDescription(String twitterDescription) { this.twitterDescription = twitterDescription; }
    public String getTwitterImage() { return twitterImage; }
    public void setTwitterImage(String twitterImage) { this.twitterImage = twitterImage; }
    public String getRobots() { return robots; }
    public void setRobots(String robots) { this.robots = robots; }
    public String getStructuredData() { return structuredData; }
    public void setStructuredData(String structuredData) { this.structuredData = structuredData; }
} 