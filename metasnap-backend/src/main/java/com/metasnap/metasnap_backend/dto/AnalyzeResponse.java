package com.metasnap.metasnap_backend.dto;

import java.util.List;

public class AnalyzeResponse {
    private int score;
    private ScoreBreakdown breakdown;
    private MetaTags meta;
    private List<FeedbackItem> feedback;
    private PreviewData previews;

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public ScoreBreakdown getBreakdown() { return breakdown; }
    public void setBreakdown(ScoreBreakdown breakdown) { this.breakdown = breakdown; }
    public MetaTags getMeta() { return meta; }
    public void setMeta(MetaTags meta) { this.meta = meta; }
    public java.util.List<FeedbackItem> getFeedback() { return feedback; }
    public void setFeedback(java.util.List<FeedbackItem> feedback) { this.feedback = feedback; }
    public PreviewData getPreviews() { return previews; }
    public void setPreviews(PreviewData previews) { this.previews = previews; }
} 