package com.trip.adviser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FlightSearchRequest(
    String origin,
    String destination,
    String date,
    @JsonProperty("desired_offers_amount") int desiredOffersAmount
) {}
