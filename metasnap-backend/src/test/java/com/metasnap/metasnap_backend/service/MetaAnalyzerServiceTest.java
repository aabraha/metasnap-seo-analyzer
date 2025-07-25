package com.metasnap.metasnap_backend.service;

import com.metasnap.metasnap_backend.dto.AnalyzeResponse;
import com.metasnap.metasnap_backend.dto.MetaTags;
import com.metasnap.metasnap_backend.dto.ScoreBreakdown;
import com.metasnap.metasnap_backend.dto.FeedbackItem;
import com.metasnap.metasnap_backend.dto.PreviewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetaAnalyzerServiceTest {

    @InjectMocks
    private MetaAnalyzerService metaAnalyzerService;

    @Test
    void testAnalyzeUrl_WithCompleteMetaTags_ShouldReturnHighScore() throws IOException {
        // Given
        String url = "https://example.com";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test Page Title</title>
                <meta name="description" content="Test description">
                <link rel="canonical" href="https://example.com">
                <meta name="robots" content="index, follow">
                <meta property="og:title" content="OG Title">
                <meta property="og:description" content="OG Description">
                <meta property="og:image" content="https://example.com/image.jpg">
                <meta name="twitter:title" content="Twitter Title">
                <meta name="twitter:description" content="Twitter Description">
                <meta name="twitter:image" content="https://example.com/twitter-image.jpg">
                <script type="application/ld+json">{"@context":"https://schema.org"}</script>
            </head>
            <body>Content</body>
            </html>
            """;

        // Create a real Document from the HTML content
        Document mockDocument = Jsoup.parse(htmlContent);
        
        // Mock the Connection and its chain
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);
        
        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);
            
            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertTrue(response.getScore() >= 80); // Should have high score with all tags
            assertNotNull(response.getMeta());
            assertNotNull(response.getBreakdown());
            assertNotNull(response.getFeedback());
            assertNotNull(response.getPreviews());
            
            // Verify meta tags
            MetaTags meta = response.getMeta();
            assertEquals("Test Page Title", meta.getTitle());
            assertEquals("Test description", meta.getDescription());
            assertEquals("https://example.com", meta.getCanonical());
            assertEquals("index, follow", meta.getRobots());
            assertEquals("OG Title", meta.getOgTitle());
            assertEquals("OG Description", meta.getOgDescription());
            assertEquals("https://example.com/image.jpg", meta.getOgImage());
            assertEquals("Twitter Title", meta.getTwitterTitle());
            assertEquals("Twitter Description", meta.getTwitterDescription());
            assertEquals("https://example.com/twitter-image.jpg", meta.getTwitterImage());
            assertNotNull(meta.getStructuredData());
        }
    }

    @Test
    void testAnalyzeUrl_WithMissingMetaTags_ShouldReturnLowScore() throws IOException {
        // Given
        String url = "https://example.com";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test Page Title</title>
            </head>
            <body>Content</body>
            </html>
            """;

        // Create a real Document from the HTML content
        Document mockDocument = Jsoup.parse(htmlContent);
        
        // Mock the Connection and its chain
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertTrue(response.getScore() <= 50); // Should have low score with missing tags
            assertNotNull(response.getMeta());
            assertEquals("Test Page Title", response.getMeta().getTitle());
            assertNull(response.getMeta().getDescription());
            assertNull(response.getMeta().getCanonical());
            assertNull(response.getMeta().getRobots());
            assertNull(response.getMeta().getOgTitle());
            assertNull(response.getMeta().getOgDescription());
            assertNull(response.getMeta().getOgImage());
            assertNull(response.getMeta().getTwitterTitle());
            assertNull(response.getMeta().getTwitterDescription());
            assertNull(response.getMeta().getTwitterImage());
            assertNull(response.getMeta().getStructuredData());
        }
    }

    @Test
    void testAnalyzeUrl_WithMissingHead_ShouldReturnZeroScore() throws IOException {
        // Given
        String url = "https://example.com";
        
        // Mock the Document to return null for head()
        Document mockDocument = mock(Document.class);
        when(mockDocument.head()).thenReturn(null);
        
        // Mock the Connection and its chain
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertEquals(0, response.getScore());
            assertNotNull(response.getMeta());
            assertNotNull(response.getFeedback());
            assertTrue(response.getFeedback().stream()
                    .anyMatch(f -> f.getMessage().contains("<head> section is missing")));
        }
    }

    @Test
    void testAnalyzeUrl_WithOpenGraphTags_ShouldCalculateCorrectScore() throws IOException {
        // Given
        String url = "https://example.com";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test Page Title</title>
                <meta property="og:title" content="OG Title">
                <meta property="og:description" content="OG Description">
                <meta property="og:image" content="https://example.com/image.jpg">
            </head>
            <body>Content</body>
            </html>
            """;

        // Create a real Document from the HTML content
        Document mockDocument = Jsoup.parse(htmlContent);
        
        // Mock the Connection and its chain
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertTrue(response.getScore() >= 25); // Title (15) + Open Graph (25) = 40
            assertNotNull(response.getMeta());
            assertEquals("OG Title", response.getMeta().getOgTitle());
            assertEquals("OG Description", response.getMeta().getOgDescription());
            assertEquals("https://example.com/image.jpg", response.getMeta().getOgImage());
            assertEquals(25, response.getBreakdown().getOpenGraph());
        }
    }

    @Test
    void testAnalyzeUrl_WithTwitterCardTags_ShouldCalculateCorrectScore() throws IOException {
        // Given
        String url = "https://example.com";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test Page Title</title>
                <meta name="twitter:title" content="Twitter Title">
                <meta name="twitter:description" content="Twitter Description">
                <meta name="twitter:image" content="https://example.com/twitter-image.jpg">
            </head>
            <body>Content</body>
            </html>
            """;

        // Create a real Document from the HTML content
        Document mockDocument = Jsoup.parse(htmlContent);
        
        // Mock the Connection and its chain
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertTrue(response.getScore() >= 25); // Title (15) + Twitter Card (20) = 35
            assertNotNull(response.getMeta());
            assertEquals("Twitter Title", response.getMeta().getTwitterTitle());
            assertEquals("Twitter Description", response.getMeta().getTwitterDescription());
            assertEquals("https://example.com/twitter-image.jpg", response.getMeta().getTwitterImage());
            assertEquals(20, response.getBreakdown().getTwitterCard());
        }
    }

    @Test
    void testAnalyzeUrl_WithStructuredData_ShouldAddScore() throws IOException {
        // Given
        String url = "https://example.com";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test Page Title</title>
                <script type="application/ld+json">{"@context":"https://schema.org","@type":"Article","headline":"Test Article"}</script>
            </head>
            <body>Content</body>
            </html>
            """;

        // Create a real Document from the HTML content
        Document mockDocument = Jsoup.parse(htmlContent);
        
        // Mock the Connection and its chain
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertTrue(response.getScore() >= 20); // Title (15) + Structured Data (10) = 25
            assertNotNull(response.getMeta());
            assertNotNull(response.getMeta().getStructuredData());
            assertTrue(response.getMeta().getStructuredData().contains("Test Article"));
            assertEquals(10, response.getBreakdown().getStructuredData());
        }
    }

    @Test
    void testAnalyzeUrl_WithInvalidUrl_ShouldHandleError() throws IOException {
        // Given
        String url = "https://invalid-url-that-does-not-exist.com";

        // Mock the Connection and its chain to throw IOException
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenThrow(new IOException("Connection failed"));

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertEquals(0, response.getScore());
            assertNotNull(response.getFeedback());
            assertTrue(response.getFeedback().stream()
                    .anyMatch(f -> f.getMessage().contains("Failed to fetch")));
        }
    }

    @Test
    void testAnalyzeUrl_WithNullUrl_ShouldHandleError() {
        // When
        AnalyzeResponse response = metaAnalyzerService.analyzeUrl(null);

        // Then
        assertNotNull(response);
        assertEquals(0, response.getScore());
        assertNotNull(response.getFeedback());
        assertTrue(response.getFeedback().stream()
                .anyMatch(f -> f.getMessage().contains("Failed to fetch")));
    }

    @Test
    void testAnalyzeUrl_WithEmptyUrl_ShouldHandleError() {
        // When
        AnalyzeResponse response = metaAnalyzerService.analyzeUrl("");

        // Then
        assertNotNull(response);
        assertEquals(0, response.getScore());
        assertNotNull(response.getFeedback());
        assertTrue(response.getFeedback().stream()
                .anyMatch(f -> f.getMessage().contains("Failed to fetch")));
    }

    @Test
    void testAnalyzeUrl_WithPartialOpenGraphTags_ShouldCalculatePartialScore() throws IOException {
        // Given
        String url = "https://example.com";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test Page Title</title>
                <meta property="og:title" content="OG Title">
                <meta property="og:description" content="OG Description">
            </head>
            <body>Content</body>
            </html>
            """;

        // Create a real Document from the HTML content
        Document mockDocument = Jsoup.parse(htmlContent);
        
        // Mock the Connection and its chain
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertTrue(response.getScore() >= 35); // Title (15) + Partial OG (20) = 35
            assertNotNull(response.getMeta());
            assertEquals("OG Title", response.getMeta().getOgTitle());
            assertEquals("OG Description", response.getMeta().getOgDescription());
            assertNull(response.getMeta().getOgImage());
            assertEquals(20, response.getBreakdown().getOpenGraph());
        }
    }

    @Test
    void testAnalyzeUrl_WithPartialTwitterCardTags_ShouldCalculatePartialScore() throws IOException {
        // Given
        String url = "https://example.com";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Test Page Title</title>
                <meta name="twitter:title" content="Twitter Title">
                <meta name="twitter:description" content="Twitter Description">
            </head>
            <body>Content</body>
            </html>
            """;

        // Create a real Document from the HTML content
        Document mockDocument = Jsoup.parse(htmlContent);
        
        // Mock the Connection and its chain
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
        when(mockConnection.followRedirects(anyBoolean())).thenReturn(mockConnection);
        when(mockConnection.get()).thenReturn(mockDocument);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            // Mock Jsoup.connect to return our mocked connection
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);

            // When
            AnalyzeResponse response = metaAnalyzerService.analyzeUrl(url);

            // Then
            assertNotNull(response);
            assertTrue(response.getScore() >= 30); // Title (15) + Partial Twitter (15) = 30
            assertNotNull(response.getMeta());
            assertEquals("Twitter Title", response.getMeta().getTwitterTitle());
            assertEquals("Twitter Description", response.getMeta().getTwitterDescription());
            assertNull(response.getMeta().getTwitterImage());
            assertEquals(15, response.getBreakdown().getTwitterCard());
        }
    }
} 