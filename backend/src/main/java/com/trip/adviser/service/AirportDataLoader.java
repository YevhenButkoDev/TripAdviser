package com.trip.adviser.service;

import com.trip.adviser.data.entity.Airport;
import com.trip.adviser.data.repository.AirportRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import java.net.URL;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;

@Service
public class AirportDataLoader implements CommandLineRunner {
    
    private final AirportRepository airportRepository;

    public AirportDataLoader(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (airportRepository.count() > 0) return;
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new URL("https://davidmegginson.github.io/ourairports-data/airports.csv").openStream()))) {
            
            reader.readNext(); // Skip header
            String[] line;
            int counter = 0;

            while ((line = reader.readNext()) != null) {
                Airport airport = new Airport();
                airport.setId(parseLong(line[0]));
                airport.setIdent(line[1]);
                airport.setType(line[2]);
                airport.setName(line[3]);
                airport.setLatitudeDeg(parseDouble(line[4]));
                airport.setLongitudeDeg(parseDouble(line[5]));
                airport.setElevationFt(parseInteger(line[6]));
                airport.setContinent(line[7]);
                airport.setIsoCountry(line[8]);
                airport.setIsoRegion(line[9]);
                airport.setMunicipality(line[10]);
                airport.setScheduledService(line[11]);
                airport.setIcaoCode(line[12]);
                airport.setIataCode(line[13]);
                airport.setGpsCode(line[14]);
                airport.setLocalCode(line[15]);
                airport.setHomeLink(line[16]);
                airport.setWikipediaLink(line[17]);
                airport.setKeywords(line[18]);
                airport.setPriority(determinePriority(line[2]));
                
                airportRepository.save(airport);
                counter++;
                System.out.println(counter);
            }
        }
    }

    private int determinePriority(String type) {
        switch (type) {
            case "large_airport": return 0;
            case "medium_airport": return 1;
            case "small_airport": return 2;
            default: return 3;
        }
    }
    
    private Long parseLong(String value) {
        return value != null && !value.trim().isEmpty() ? Long.parseLong(value) : null;
    }
    
    private Double parseDouble(String value) {
        return value != null && !value.trim().isEmpty() ? Double.parseDouble(value) : null;
    }
    
    private Integer parseInteger(String value) {
        return value != null && !value.trim().isEmpty() ? Integer.parseInt(value) : null;
    }
}
