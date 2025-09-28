package com.trip.adviser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AskRequest(
    String question,
    @JsonProperty("departure_date") String departureDate,
    @JsonProperty("adults_amount") Integer adultsAmount,
    @JsonProperty("children_amount") Integer childrenAmount,
    @JsonProperty("ticket_class") String ticketClass,
    @JsonProperty("amount_of_nights") Integer amountOfNights,
    @JsonProperty("budget_start") Double budgetStart,
    @JsonProperty("budget_end") Double budgetEnd
) {}
