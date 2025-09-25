package com.trip.adviser.controller;

import org.springframework.web.bind.annotation.*;

import com.trip.adviser.dto.*;
import com.trip.adviser.service.*;

@RestController
public class TripController {
    private final OpenAiExtractorService extractor;

    public TripController(OpenAiExtractorService extractor) {
        this.extractor = extractor;
    }

    @PostMapping("/api/extract-intent")
    public TripIntent extract(@RequestBody AskRequest req) {
        var result = extractor.extract(req.question());
        System.out.println(result);
        return result;
    }
}
