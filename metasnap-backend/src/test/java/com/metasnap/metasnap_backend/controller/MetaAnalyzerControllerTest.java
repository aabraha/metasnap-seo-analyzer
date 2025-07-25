package com.metasnap.metasnap_backend.controller;

import com.metasnap.metasnap_backend.dto.AnalyzeRequest;
import com.metasnap.metasnap_backend.dto.AnalyzeResponse;
import com.metasnap.metasnap_backend.dto.MetaTags;
import com.metasnap.metasnap_backend.dto.ScoreBreakdown;
import com.metasnap.metasnap_backend.dto.FeedbackItem;
import com.metasnap.metasnap_backend.dto.PreviewData;
import com.metasnap.metasnap_backend.service.MetaAnalyzerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MetaAnalyzerControllerTest {

    @Mock
    private MetaAnalyzerService metaAnalyzerService;

    @InjectMocks
    private MetaAnalyzerController metaAnalyzerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(metaAnalyzerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAnalyze_WithValidUrl_ShouldReturnSuccessResponse() throws Exception {
        // Given
        String url = "https://example.com";
        AnalyzeRequest request = new AnalyzeRequest();
        request.setUrl(url);

        AnalyzeResponse expectedResponse = createSampleAnalyzeResponse();
        when(metaAnalyzerService.analyzeUrl(url)).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.score").value(expectedResponse.getScore()))
                .andExpect(jsonPath("$.meta.title").value(expectedResponse.getMeta().getTitle()))
                .andExpect(jsonPath("$.meta.description").value(expectedResponse.getMeta().getDescription()))
                .andExpect(jsonPath("$.breakdown.titleDescription").value(expectedResponse.getBreakdown().getTitleDescription()))
                .andExpect(jsonPath("$.feedback").isArray())
                .andExpect(jsonPath("$.previews.googlePreview").value(expectedResponse.getPreviews().getGooglePreview()));
    }

    @Test
    void testAnalyze_WithEmptyUrl_ShouldReturnBadRequest() throws Exception {
        // Given
        AnalyzeRequest request = new AnalyzeRequest();
        request.setUrl("");

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAnalyze_WithNullUrl_ShouldReturnBadRequest() throws Exception {
        // Given
        AnalyzeRequest request = new AnalyzeRequest();
        request.setUrl(null);

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAnalyze_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
        // Given
        String invalidJson = "{ invalid json }";

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAnalyze_WithMissingUrlField_ShouldReturnBadRequest() throws Exception {
        // Given
        String jsonWithoutUrl = "{}";

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithoutUrl))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAnalyze_WithHighScoreResponse_ShouldReturnCorrectScore() throws Exception {
        // Given
        String url = "https://example.com";
        AnalyzeRequest request = new AnalyzeRequest();
        request.setUrl(url);

        AnalyzeResponse highScoreResponse = createHighScoreResponse();
        when(metaAnalyzerService.analyzeUrl(url)).thenReturn(highScoreResponse);

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(85))
                .andExpect(jsonPath("$.breakdown.titleDescription").value(30))
                .andExpect(jsonPath("$.breakdown.canonicalRobots").value(15))
                .andExpect(jsonPath("$.breakdown.openGraph").value(25))
                .andExpect(jsonPath("$.breakdown.twitterCard").value(15));
    }

    @Test
    void testAnalyze_WithLowScoreResponse_ShouldReturnCorrectScore() throws Exception {
        // Given
        String url = "https://example.com";
        AnalyzeRequest request = new AnalyzeRequest();
        request.setUrl(url);

        AnalyzeResponse lowScoreResponse = createLowScoreResponse();
        when(metaAnalyzerService.analyzeUrl(url)).thenReturn(lowScoreResponse);

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(15))
                .andExpect(jsonPath("$.breakdown.titleDescription").value(15))
                .andExpect(jsonPath("$.breakdown.canonicalRobots").value(0))
                .andExpect(jsonPath("$.breakdown.openGraph").value(0))
                .andExpect(jsonPath("$.breakdown.twitterCard").value(0));
    }

    @Test
    void testAnalyze_WithCompleteMetaTags_ShouldReturnAllFields() throws Exception {
        // Given
        String url = "https://example.com";
        AnalyzeRequest request = new AnalyzeRequest();
        request.setUrl(url);

        AnalyzeResponse completeResponse = createCompleteResponse();
        when(metaAnalyzerService.analyzeUrl(url)).thenReturn(completeResponse);

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.title").value("Complete Page Title"))
                .andExpect(jsonPath("$.meta.description").value("Complete page description"))
                .andExpect(jsonPath("$.meta.canonical").value("https://example.com"))
                .andExpect(jsonPath("$.meta.robots").value("index, follow"))
                .andExpect(jsonPath("$.meta.ogTitle").value("OG Title"))
                .andExpect(jsonPath("$.meta.ogDescription").value("OG Description"))
                .andExpect(jsonPath("$.meta.ogImage").value("https://example.com/image.jpg"))
                .andExpect(jsonPath("$.meta.twitterTitle").value("Twitter Title"))
                .andExpect(jsonPath("$.meta.twitterDescription").value("Twitter Description"))
                .andExpect(jsonPath("$.meta.twitterImage").value("https://example.com/twitter.jpg"))
                .andExpect(jsonPath("$.meta.structuredData").exists());
    }

    @Test
    void testAnalyze_WithFeedbackItems_ShouldReturnCorrectFeedback() throws Exception {
        // Given
        String url = "https://example.com";
        AnalyzeRequest request = new AnalyzeRequest();
        request.setUrl(url);

        AnalyzeResponse responseWithFeedback = createResponseWithFeedback();
        when(metaAnalyzerService.analyzeUrl(url)).thenReturn(responseWithFeedback);

        // When & Then
        mockMvc.perform(post("/api/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback").isArray())
                .andExpect(jsonPath("$.feedback[0].type").value("good"))
                .andExpect(jsonPath("$.feedback[0].message").value("Title tag is present"))
                .andExpect(jsonPath("$.feedback[1].type").value("warning"))
                .andExpect(jsonPath("$.feedback[1].message").value("Description meta tag is missing"));
    }

    // Helper methods to create test data
    private AnalyzeResponse createSampleAnalyzeResponse() {
        AnalyzeResponse response = new AnalyzeResponse();
        response.setScore(75);
        
        MetaTags meta = new MetaTags();
        meta.setTitle("Sample Page Title");
        meta.setDescription("Sample page description");
        response.setMeta(meta);
        
        ScoreBreakdown breakdown = new ScoreBreakdown();
        breakdown.setTitleDescription(30);
        breakdown.setCanonicalRobots(15);
        breakdown.setOpenGraph(20);
        breakdown.setTwitterCard(10);
        response.setBreakdown(breakdown);
        
        List<FeedbackItem> feedback = new ArrayList<>();
        FeedbackItem item1 = new FeedbackItem();
        item1.setType("good");
        item1.setMessage("Title tag is present");
        feedback.add(item1);
        response.setFeedback(feedback);
        
        PreviewData previews = new PreviewData();
        previews.setGooglePreview("Sample Page Title\nhttps://example.com\nSample page description");
        previews.setFacebookPreview("OG Title\nOG Description");
        previews.setTwitterPreview("Twitter Title\nTwitter Description");
        response.setPreviews(previews);
        
        return response;
    }

    private AnalyzeResponse createErrorResponse() {
        AnalyzeResponse response = new AnalyzeResponse();
        response.setScore(0);
        response.setMeta(new MetaTags());
        response.setBreakdown(new ScoreBreakdown());
        
        List<FeedbackItem> feedback = new ArrayList<>();
        FeedbackItem item = new FeedbackItem();
        item.setType("missing");
        item.setMessage("Failed to fetch or parse the URL");
        feedback.add(item);
        response.setFeedback(feedback);
        
        response.setPreviews(new PreviewData());
        return response;
    }

    private AnalyzeResponse createHighScoreResponse() {
        AnalyzeResponse response = new AnalyzeResponse();
        response.setScore(85);
        
        ScoreBreakdown breakdown = new ScoreBreakdown();
        breakdown.setTitleDescription(30);
        breakdown.setCanonicalRobots(15);
        breakdown.setOpenGraph(25);
        breakdown.setTwitterCard(15);
        response.setBreakdown(breakdown);
        
        response.setMeta(new MetaTags());
        response.setFeedback(new ArrayList<>());
        response.setPreviews(new PreviewData());
        
        return response;
    }

    private AnalyzeResponse createLowScoreResponse() {
        AnalyzeResponse response = new AnalyzeResponse();
        response.setScore(15);
        
        ScoreBreakdown breakdown = new ScoreBreakdown();
        breakdown.setTitleDescription(15);
        breakdown.setCanonicalRobots(0);
        breakdown.setOpenGraph(0);
        breakdown.setTwitterCard(0);
        response.setBreakdown(breakdown);
        
        response.setMeta(new MetaTags());
        response.setFeedback(new ArrayList<>());
        response.setPreviews(new PreviewData());
        
        return response;
    }

    private AnalyzeResponse createCompleteResponse() {
        AnalyzeResponse response = new AnalyzeResponse();
        response.setScore(100);
        
        MetaTags meta = new MetaTags();
        meta.setTitle("Complete Page Title");
        meta.setDescription("Complete page description");
        meta.setCanonical("https://example.com");
        meta.setRobots("index, follow");
        meta.setOgTitle("OG Title");
        meta.setOgDescription("OG Description");
        meta.setOgImage("https://example.com/image.jpg");
        meta.setTwitterTitle("Twitter Title");
        meta.setTwitterDescription("Twitter Description");
        meta.setTwitterImage("https://example.com/twitter.jpg");
        meta.setStructuredData("{\"@context\":\"https://schema.org\"}");
        response.setMeta(meta);
        
        ScoreBreakdown breakdown = new ScoreBreakdown();
        breakdown.setTitleDescription(30);
        breakdown.setCanonicalRobots(15);
        breakdown.setOpenGraph(25);
        breakdown.setTwitterCard(20);
        breakdown.setStructuredData(10);
        response.setBreakdown(breakdown);
        
        response.setFeedback(new ArrayList<>());
        response.setPreviews(new PreviewData());
        
        return response;
    }

    private AnalyzeResponse createResponseWithFeedback() {
        AnalyzeResponse response = new AnalyzeResponse();
        response.setScore(15);
        
        List<FeedbackItem> feedback = new ArrayList<>();
        
        FeedbackItem item1 = new FeedbackItem();
        item1.setType("good");
        item1.setMessage("Title tag is present");
        feedback.add(item1);
        
        FeedbackItem item2 = new FeedbackItem();
        item2.setType("warning");
        item2.setMessage("Description meta tag is missing");
        feedback.add(item2);
        
        response.setFeedback(feedback);
        response.setMeta(new MetaTags());
        response.setBreakdown(new ScoreBreakdown());
        response.setPreviews(new PreviewData());
        
        return response;
    }
} 