package com.metasnap.metasnap_backend.service;

import com.metasnap.metasnap_backend.dto.AnalyzeResponse;
import com.metasnap.metasnap_backend.dto.MetaTags;
import com.metasnap.metasnap_backend.dto.ScoreBreakdown;
import com.metasnap.metasnap_backend.dto.FeedbackItem;
import com.metasnap.metasnap_backend.dto.PreviewData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MetaAnalyzerService {
    public AnalyzeResponse analyzeUrl(String url) {
        AnalyzeResponse response = new AnalyzeResponse();
        MetaTags meta = new MetaTags();
        ScoreBreakdown breakdown = new ScoreBreakdown();
        List<FeedbackItem> feedback = new ArrayList<>();
        PreviewData previews = new PreviewData();
        int score = 0;
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; MetaSnapBot/1.0)")
                    .timeout(10000)
                    .followRedirects(true)
                    .get();
            Element head = doc.head();
            if (head == null) {
                addFeedback(feedback, "missing", "<head> section is missing");
                response.setScore(0);
                response.setBreakdown(breakdown);
                response.setMeta(meta);
                response.setFeedback(feedback);
                response.setPreviews(previews);
                return response;
            }
            // Title
            String title = head.selectFirst("title") != null ? head.selectFirst("title").text() : null;
            meta.setTitle(title);
            if (title != null && !title.isEmpty()) {
                score += 15;
                breakdown.setTitleDescription(breakdown.getTitleDescription() + 15);
                addFeedback(feedback, "good", "Title tag is present");
            } else {
                addFeedback(feedback, "missing", "Title tag is missing");
            }
            // Description
            String description = getMetaContent(head, "description");
            meta.setDescription(description);
            if (description != null && !description.isEmpty()) {
                score += 15;
                breakdown.setTitleDescription(breakdown.getTitleDescription() + 15);
                addFeedback(feedback, "good", "Description meta tag is present");
            } else {
                addFeedback(feedback, "warning", "Description meta tag is missing");
            }
            // Canonical
            String canonical = getLinkHref(head, "canonical");
            meta.setCanonical(canonical);
            if (canonical != null && !canonical.isEmpty()) {
                score += 10;
                breakdown.setCanonicalRobots(breakdown.getCanonicalRobots() + 10);
                addFeedback(feedback, "good", "Canonical tag is present");
            } else {
                addFeedback(feedback, "warning", "Canonical tag is missing");
            }
            // Robots
            String robots = getMetaContent(head, "robots");
            meta.setRobots(robots);
            if (robots != null && !robots.isEmpty()) {
                score += 5;
                breakdown.setCanonicalRobots(breakdown.getCanonicalRobots() + 5);
                addFeedback(feedback, "good", "Robots meta tag is present");
            } else {
                addFeedback(feedback, "warning", "Robots meta tag is missing");
            }
            // Open Graph
            String ogTitle = getMetaContent(head, "og:title");
            String ogDescription = getMetaContent(head, "og:description");
            String ogImage = getMetaContent(head, "og:image");
            meta.setOgTitle(ogTitle);
            meta.setOgDescription(ogDescription);
            meta.setOgImage(ogImage);
            int ogScore = 0;
            if (ogTitle != null && !ogTitle.isEmpty()) ogScore += 10;
            if (ogDescription != null && !ogDescription.isEmpty()) ogScore += 10;
            if (ogImage != null && !ogImage.isEmpty()) ogScore += 5;
            breakdown.setOpenGraph(ogScore);
            score += ogScore;
            if (ogScore > 0) {
                addFeedback(feedback, ogScore == 25 ? "good" : "warning", "Open Graph tags: " + ogScore + "/25");
            } else {
                addFeedback(feedback, "missing", "Open Graph tags are missing");
            }
            // Twitter Card
            String twitterTitle = getMetaContent(head, "twitter:title");
            String twitterDescription = getMetaContent(head, "twitter:description");
            String twitterImage = getMetaContent(head, "twitter:image");
            meta.setTwitterTitle(twitterTitle);
            meta.setTwitterDescription(twitterDescription);
            meta.setTwitterImage(twitterImage);
            int twitterScore = 0;
            if (twitterTitle != null && !twitterTitle.isEmpty()) twitterScore += 8;
            if (twitterDescription != null && !twitterDescription.isEmpty()) twitterScore += 7;
            if (twitterImage != null && !twitterImage.isEmpty()) twitterScore += 5;
            breakdown.setTwitterCard(twitterScore);
            score += twitterScore;
            if (twitterScore > 0) {
                addFeedback(feedback, twitterScore == 20 ? "good" : "warning", "Twitter Card tags: " + twitterScore + "/20");
            } else {
                addFeedback(feedback, "missing", "Twitter Card tags are missing");
            }
            // Structured Data (JSON-LD)
            String structuredData = null;
            Elements scripts = head.select("script[type=application/ld+json]");
            if (!scripts.isEmpty()) {
                structuredData = scripts.first().html();
                score += 10;
                breakdown.setStructuredData(10);
                addFeedback(feedback, "good", "Structured data (JSON-LD) is present");
            } else {
                addFeedback(feedback, "warning", "Structured data (JSON-LD) is missing");
            }
            meta.setStructuredData(structuredData);
            // Previews (simple text for now)
            previews.setGooglePreview(meta.getTitle() + "\n" + url + "\n" + meta.getDescription());
            previews.setFacebookPreview(meta.getOgTitle() + "\n" + meta.getOgDescription());
            previews.setTwitterPreview(meta.getTwitterTitle() + "\n" + meta.getTwitterDescription());
        } catch (Exception e) {
            addFeedback(feedback, "missing", "Failed to fetch or parse the URL: " + e.getMessage());
            response.setScore(0);
            response.setBreakdown(new ScoreBreakdown());
            response.setMeta(new MetaTags());
            response.setFeedback(feedback);
            response.setPreviews(new PreviewData());
            return response;
        }
        response.setScore(score);
        response.setBreakdown(breakdown);
        response.setMeta(meta);
        response.setFeedback(feedback);
        response.setPreviews(previews);
        return response;
    }

    private String getMetaContent(Element head, String name) {
        Element tag = head.selectFirst("meta[name=" + name + "]");
        if (tag == null) tag = head.selectFirst("meta[property=" + name + "]");
        return tag != null ? tag.attr("content") : null;
    }

    private String getLinkHref(Element head, String rel) {
        Element tag = head.selectFirst("link[rel=" + rel + "]");
        return tag != null ? tag.attr("href") : null;
    }

    private void addFeedback(List<FeedbackItem> feedback, String type, String message) {
        FeedbackItem item = new FeedbackItem();
        item.setType(type);
        item.setMessage(message);
        feedback.add(item);
    }
} 