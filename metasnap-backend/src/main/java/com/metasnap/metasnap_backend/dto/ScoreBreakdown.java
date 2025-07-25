package com.metasnap.metasnap_backend.dto;

public class ScoreBreakdown {
    private int titleDescription;
    private int openGraph;
    private int twitterCard;
    private int canonicalRobots;
    private int structuredData;

    public int getTitleDescription() { return titleDescription; }
    public void setTitleDescription(int titleDescription) { this.titleDescription = titleDescription; }
    public int getOpenGraph() { return openGraph; }
    public void setOpenGraph(int openGraph) { this.openGraph = openGraph; }
    public int getTwitterCard() { return twitterCard; }
    public void setTwitterCard(int twitterCard) { this.twitterCard = twitterCard; }
    public int getCanonicalRobots() { return canonicalRobots; }
    public void setCanonicalRobots(int canonicalRobots) { this.canonicalRobots = canonicalRobots; }
    public int getStructuredData() { return structuredData; }
    public void setStructuredData(int structuredData) { this.structuredData = structuredData; }
} 