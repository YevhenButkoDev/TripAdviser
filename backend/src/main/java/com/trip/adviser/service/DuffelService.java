package com.trip.adviser.service;

import com.trip.adviser.dto.DuffelOfferRequest;
import com.trip.adviser.dto.DuffelOfferResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.*;
import java.util.stream.*;

@Service
public class DuffelService {
    
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;
    
    public DuffelService(RestTemplate restTemplate,
                        @Value("${duffel.api-key}") String apiKey,
                        @Value("${duffel.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }
    
    public DuffelOfferResponse getFlightOffers(
        String originCity,
        String destinationCity,
        String departureDate,
        String cabinClass,
        int adults,
        int children,
        int nights,
        int desiredOffersAmount
    ) {
        String url = baseUrl + "/air/offer_requests";

        var adultPassengers = IntStream.range(0, adults)
            .mapToObj((n) -> new DuffelOfferRequest.Passenger("adult"))
            .toList();

        var childPassengers = IntStream.range(0, children)
            .mapToObj((n) -> new DuffelOfferRequest.Passenger("child"))
            .toList();

        var passengers = new ArrayList<DuffelOfferRequest.Passenger>();
        passengers.addAll(adultPassengers);
        passengers.addAll(childPassengers);
        
        DuffelOfferRequest.Data data = new DuffelOfferRequest.Data(
            cabinClass,
            passengers,
            List.of(
                new DuffelOfferRequest.Slice(originCity, destinationCity, departureDate),
                new DuffelOfferRequest.Slice(destinationCity, originCity, LocalDate.parse(departureDate).plusDays(nights).toString())
            )
        );
        
        DuffelOfferRequest request = new DuffelOfferRequest(data);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");
        headers.set("Duffel-Version", "v2");
        
        HttpEntity<DuffelOfferRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            DuffelOfferResponse response = restTemplate.exchange(url, HttpMethod.POST, entity, DuffelOfferResponse.class).getBody();
            
            if (response != null && response.data() != null && response.data().offers() != null) {
                List<DuffelOfferResponse.Offer> sortedOffers = response.data().offers().stream()
                    .sorted(Comparator.comparingDouble(o -> Double.parseDouble(o.totalAmount())))
                    .limit(desiredOffersAmount)
                    .toList();
                
                return new DuffelOfferResponse(new DuffelOfferResponse.Data(sortedOffers));
            }
            
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch flight offers", e);
        }
    }
}
