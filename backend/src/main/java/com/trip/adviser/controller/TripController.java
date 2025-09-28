package com.trip.adviser.controller;

import org.springframework.web.bind.annotation.*;

import com.trip.adviser.dto.*;
import com.trip.adviser.service.*;

@RestController
public class TripController {
    private final OpenAiExtractorService extractor;
    private final DuffelService duffelService;
    private final TravelDataCollectorService travelDataCollectorService;

    public TripController(OpenAiExtractorService extractor, DuffelService duffelService, TravelDataCollectorService travelDataCollectorService) {
        this.extractor = extractor;
        this.duffelService = duffelService;
        this.travelDataCollectorService = travelDataCollectorService;
    }

    @PostMapping("/api/build-trip")
    public TravelData buildTrip(@RequestBody AskRequest req) {
        var result = travelDataCollectorService.collectData(req);
        System.out.println(result);
        return result;
    }

    @PostMapping("/api/flights")
    public DuffelOfferResponse getFlights(@RequestBody FlightSearchRequest request) {
        return duffelService.getFlightOffers(
            request.origin(), 
            request.destination(), 
            request.date(),
            "economy",
            1,
            0,
            5,
            request.desiredOffersAmount()
        );
    }
}
