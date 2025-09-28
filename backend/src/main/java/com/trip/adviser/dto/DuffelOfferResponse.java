package com.trip.adviser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record DuffelOfferResponse(Data data) {
    
    public record Data(List<Offer> offers) {}
    
    public record Offer(
        String id,
        @JsonProperty("total_amount") String totalAmount,
        @JsonProperty("total_currency") String totalCurrency,
        @JsonProperty("base_amount") String baseAmount,
        @JsonProperty("base_currency") String baseCurrency,
        @JsonProperty("tax_amount") String taxAmount,
        @JsonProperty("tax_currency") String taxCurrency,
        @JsonProperty("expires_at") String expiresAt,
        List<Slice> slices,
        List<Passenger> passengers,
        Carrier owner
    ) {}
    
    public record Slice(
        String id,
        Airport origin,
        Airport destination,
        String duration,
        @JsonProperty("fare_brand_name") String fareBrandName,
        List<Segment> segments
    ) {}
    
    public record Segment(
        String id,
        Airport origin,
        Airport destination,
        @JsonProperty("departing_at") String departingAt,
        @JsonProperty("arriving_at") String arrivingAt,
        String duration,
        Aircraft aircraft,
        @JsonProperty("marketing_carrier") Carrier marketingCarrier,
        @JsonProperty("operating_carrier") Carrier operatingCarrier
    ) {}
    
    public record Airport(
        @JsonProperty("iata_code") String iataCode,
        @JsonProperty("city_name") String cityName,
        String name
    ) {}
    
    public record Aircraft(
        @JsonProperty("iata_code") String iataCode,
        String name
    ) {}
    
    public record Carrier(
        String name, 
        @JsonProperty("iata_code") String iataCode
    ) {}
    
    public record Passenger(
        String id,
        String type
    ) {}
}
