package com.trip.adviser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DuffelOfferRequest(Data data) {
    
    public record Data(
        @JsonProperty("cabin_class") String cabinClass,
        List<Passenger> passengers,
        List<Slice> slices
    ) {}
    
    public record Passenger(String type) {}
    
    public record Slice(
        String origin,
        String destination,
        @JsonProperty("departure_date") String departureDate
    ) {}
}
