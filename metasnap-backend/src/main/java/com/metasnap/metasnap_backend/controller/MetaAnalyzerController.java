package com.metasnap.metasnap_backend.controller;

import com.metasnap.metasnap_backend.dto.AnalyzeRequest;
import com.metasnap.metasnap_backend.dto.AnalyzeResponse;
import com.metasnap.metasnap_backend.service.MetaAnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class MetaAnalyzerController {

    private final MetaAnalyzerService metaAnalyzerService;

    public MetaAnalyzerController(MetaAnalyzerService metaAnalyzerService) {
        this.metaAnalyzerService = metaAnalyzerService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@Valid @RequestBody AnalyzeRequest request) {
        AnalyzeResponse response = metaAnalyzerService.analyzeUrl(request.getUrl());
        return ResponseEntity.ok(response);
    }
} 