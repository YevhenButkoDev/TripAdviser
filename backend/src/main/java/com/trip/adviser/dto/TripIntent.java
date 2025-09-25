package com.trip.adviser.dto;

import java.util.*;

import com.fasterxml.jackson.annotation.*;

public record TripIntent(
    String intent,
    @JsonProperty("origin_city") String originCity,
    @JsonProperty("origin_city_iata") String originCityIata,
    @JsonProperty("travel_window") TravelWindow travelWindow,
    @JsonProperty("budget_eur") Double budgetEur,
    Party party,
    // Accept [] or null -> empty list
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<String> destinations,

    @JsonProperty("destinations_iata")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<String> destinationsIata,

    // Accept [] or null -> empty list
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<String> waypoints
) {
    public record TravelWindow(
        String month,
        @JsonProperty("duration_nights") DurationNights durationNights
    ) {}

    public record DurationNights(Integer min, Integer max) {}

    public record Party(Integer adults, Integer children) {}
}
