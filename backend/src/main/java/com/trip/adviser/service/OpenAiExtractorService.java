package com.trip.adviser.service;

import java.util.*;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.*;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.adviser.data.repository.*;
import com.trip.adviser.dto.TripIntent;
import com.trip.adviser.dto.DuffelOfferResponse;

@Service
public class OpenAiExtractorService {

    private final ChatClient chatClient;
    private final ObjectMapper mapper;

    private static final String SYSTEM_PROMPT = """
        You are GeoTravel AI, an expert travel planning assistant specialized in finding optimal destinations based on weather, seasonality, and budget constraints.

# MISSION
Analyze the user's travel request and recommend 3 suitable destinations that match their criteria, with detailed reasoning for each choice.

# INPUT ANALYSIS RULES
1. Extract all explicit parameters from the user's request
2. Infer missing parameters using intelligent defaults:
   - If no duration: suggest 7-10 nights for international, 3-5 for regional
   - If no party size: assume 2 adults
   - Translate all non-English terms to English
3. Consider current seasonality and weather patterns
4. Factor in budget constraints for flights + accommodation

# DESTINATION SELECTION CRITERIA
- **Weather Compatibility**: Match desired climate/season
- **Budget Feasibility**: Ensure destinations are realistic within budget
- **Travel Practicality**: Reasonable flight distances and logistics
- **Experience Match**: Align with stated preferences (beach, culture, etc.)
- **Seasonal Appeal**: Highlight what's particularly good during travel window

# OUTPUT REQUIREMENTS
Return ONLY valid JSON with this exact structure:

```json
{
  "intent": "string (beach_vacation, city_break, adventure, cultural, etc.)",
  "origin_city": "string",
  "destinations": [
    {
      "destination_country": "string",
      "destination_city": "string (in case destination city does not have airport pick the nearest city with airport)",
      "description": "string (3-4 sentences explaining why this destination is ideal - include weather conditions, unique attractions, value proposition, and how it matches user criteria)",
      "budget_suitability": "excellent|good|moderate",
      "weather_conditions": "string (describe expected weather)",
      "key_attractions": ["string array"],
      "estimated_flight_cost_eur": number,
      "estimated_accommodation_cost_eur": "number (per night)"
    }
  ]
}
    """;

    public OpenAiExtractorService(ChatClient.Builder chatClientBuilder, ObjectMapper mapper, DuffelService duffelService, AirportRepository airportRepository) {
        this.chatClient = chatClientBuilder
            .defaultOptions(OpenAiChatOptions.builder()
                .temperature(0.2)
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT).build())
                .build())
            .build();
        this.mapper = mapper;
    }

    public TripIntent extract(String userQuestion) {
        ChatResponse response = chatClient.prompt()
            .system(SYSTEM_PROMPT)
            .user(userQuestion)
            .call()
            .chatResponse();

        String content = response.getResult().getOutput().getText();
        
        try {
            return mapper.readValue(content, TripIntent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }
}
