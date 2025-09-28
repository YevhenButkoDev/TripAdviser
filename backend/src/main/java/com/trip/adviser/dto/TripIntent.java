package com.trip.adviser.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

public record TripIntent(
    String intent,
    @JsonProperty("origin_city") String originCity,
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<Destination> destinations
) {
    public record Destination(
        @JsonProperty("destination_country") String destinationCountry,
        @JsonProperty("destination_city") String destinationCity,
        String description,
        @JsonProperty("budget_suitability") String budgetSuitability,
        @JsonProperty("weather_conditions") String weatherConditions,
        @JsonProperty("key_attractions") List<String> keyAttractions,
        @JsonProperty("estimated_flight_cost_eur") Integer estimatedFlightCostEur,
        @JsonProperty("estimated_accommodation_cost_eur") Integer estimatedAccommodationCostEur
    ) {}
}
