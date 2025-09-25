package com.trip.adviser.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.trip.adviser.dto.*;

import lombok.extern.slf4j.*;

@Slf4j
@Service
public class OpenAiExtractorService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String model;
    private final String apiBase;
    private final String apiKey;

    private static final String SYSTEM_PROMPT = """
        Transform the user’s phrase into JSON trip slots. Any non english fields translate to english.
        Fields: intent, origin_city, origin_city_iata(city in iata format) travel_window.month, budget_eur, duration_nights(min,max), party(adults,children), destinations(array of values, can be specific cities, countries, or abstract like sea or mountains), destinations_iata(the same as destinations but in iata format, applies only when destination is a city), waypoints(array of values, desired cities to visit)
    """;

    public OpenAiExtractorService(
        RestTemplate restTemplate,
        ObjectMapper mapper,
        @Value("${app.openai.api-base}") String apiBase,
        @Value("${app.openai.model}") String model,
        @Value("${OPENAI_API_KEY:}") String apiKey
    ) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.apiBase = apiBase;
        this.model = model;
        this.apiKey = apiKey;
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY env var is required");
        }
    }

    public TripIntent extract(String userQuestion) {
        // Build request
        Map<String, Object> body = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", userQuestion)
            ),
            "temperature", 0.2,
            "response_format", Map.of("type", "json_object")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        String rawResponse = restTemplate.postForObject(
            apiBase + "/chat/completions",
            entity,
            String.class
        );

        String content = extractContent(rawResponse);

        try {
            return mapper.readValue(content, TripIntent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractContent(String rawJson) {
        try {
            JsonNode tree = mapper.readTree(rawJson);
            return tree.path("choices").get(0).path("message").path("content").asText("{}");
        } catch (Exception e) {
            return "{}";
        }
    }
}
