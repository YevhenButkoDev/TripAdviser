package com.trip.adviser.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.*;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.adviser.dto.TripIntent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpenAiExtractorService {

    private final ChatClient chatClient;
    private final ObjectMapper mapper;

    private static final String SYSTEM_PROMPT = """
        Transform the user's phrase into JSON trip slots. Any non english fields translate to english.
        Fields: intent, origin_city, origin_city_iata(city in iata format) travel_window.month, budget_eur, duration_nights(min,max), party(adults,children), destinations(array of values, can be specific cities, countries, or abstract like sea or mountains), destinations_iata(the same as destinations but in iata format, applies only when destination is a city), waypoints(array of values, desired cities to visit)
    """;

    public OpenAiExtractorService(ChatClient.Builder chatClientBuilder, ObjectMapper mapper) {
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
            throw new RuntimeException(e);
        }
    }
}
