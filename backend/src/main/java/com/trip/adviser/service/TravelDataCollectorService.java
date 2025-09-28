package com.trip.adviser.service;

import org.springframework.stereotype.*;

import com.trip.adviser.data.repository.*;
import com.trip.adviser.dto.*;

@Service
public class TravelDataCollectorService {

    private final OpenAiExtractorService openAiExtractorService;
    private final DuffelService duffelService;
    private final AirportRepository airportRepository;

    public TravelDataCollectorService(OpenAiExtractorService openAiExtractorService, DuffelService duffelService, AirportRepository airportRepository) {
        this.openAiExtractorService = openAiExtractorService;
        this.duffelService = duffelService;
        this.airportRepository = airportRepository;
    }

    public TravelData collectData(AskRequest askRequest) {
        var tripIntent = openAiExtractorService.extract(askRequest.question());
        var flightOffers = tripIntent.destinations().stream()
            .map(destination -> {
                var originAirport = airportRepository.findAirportsByCity(tripIntent.originCity());
                var destinationAirport = airportRepository.findAirportsByCity(destination.destinationCity());

                if (originAirport == null || destinationAirport == null) {
                    return null;
                }

                return duffelService.getFlightOffers(
                    originAirport.getIataCode(),
                    destinationAirport.getIataCode(),
                    askRequest.departureDate(),
                    askRequest.ticketClass(),
                    askRequest.adultsAmount(),
                    askRequest.childrenAmount(),
                    askRequest.amountOfNights(),
                    3 // Get 3 cheapest offers
                );
            })
            .toList();
        return new TravelData(tripIntent, flightOffers);
    }
}
