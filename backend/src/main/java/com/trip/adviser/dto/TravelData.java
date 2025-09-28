package com.trip.adviser.dto;

import java.util.*;

public record TravelData(TripIntent tripMeta, List<DuffelOfferResponse> flightOffers) {
}
