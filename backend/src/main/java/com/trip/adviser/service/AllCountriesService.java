package com.trip.adviser.service;

import java.io.*;
import java.time.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.trip.adviser.data.entity.*;
import com.trip.adviser.data.repository.*;

@Service
public class AllCountriesService {

    @Autowired
    private GeonameRepository repository;

    public void loadGeonamesFromFile(String filePath) {
        List<Geoname> geonames = new ArrayList<>();
        int batchSize = 1000; // Batch size for saving to database
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] fields = line.split("\t", -1); // -1 to keep empty fields

                // Ensure the line has exactly 19 fields
                if (fields.length != 19) {
                    System.err.println("Skipping line " + lineNumber + ": Incorrect number of fields (" + fields.length + ")");
                    continue;
                }

                try {
                    Geoname geoname = new Geoname();
                    geoname.setGeonameId(parseInteger(fields[0], lineNumber));
                    geoname.setName(fields[1].isEmpty() ? null : fields[1]);
                    geoname.setAsciiName(fields[2].isEmpty() ? null : fields[2]);
                    geoname.setAlternateNames(fields[3].isEmpty() ? null : fields[3]);
                    geoname.setLatitude(parseDouble(fields[4], lineNumber));
                    geoname.setLongitude(parseDouble(fields[5], lineNumber));
                    geoname.setFeatureClass(fields[6].isEmpty() ? null : fields[6]);
                    geoname.setFeatureCode(fields[7].isEmpty() ? null : fields[7]);
                    geoname.setCountryCode(fields[8].isEmpty() ? null : fields[8]);
                    geoname.setCc2(fields[9].isEmpty() ? null : fields[9]);
                    geoname.setAdmin1Code(fields[10].isEmpty() ? null : fields[10]);
                    geoname.setAdmin2Code(fields[11].isEmpty() ? null : fields[11]);
                    geoname.setAdmin3Code(fields[12].isEmpty() ? null : fields[12]);
                    geoname.setAdmin4Code(fields[13].isEmpty() ? null : fields[13]);
                    geoname.setPopulation(parseLong(fields[14], lineNumber));
                    geoname.setElevation(parseInteger(fields[15], lineNumber));
                    geoname.setDem(parseInteger(fields[16], lineNumber));
                    geoname.setTimezone(fields[17].isEmpty() ? null : fields[17]);
                    geoname.setModificationDate(fields[18].isEmpty() ? null : LocalDate.parse(fields[18]));

                    geonames.add(geoname);

                    // Save in batches
                    if (geonames.size() >= batchSize) {
                        repository.saveAll(geonames);
                        System.out.println("Saved batch of " + geonames.size() + " records at line " + lineNumber);
                        geonames.clear();
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + e.getMessage());
                }
            }

            // Save remaining records
            if (!geonames.isEmpty()) {
                repository.saveAll(geonames);
                System.out.println("Saved final batch of " + geonames.size() + " records.");
            }

            System.out.println("Data loading completed. Processed " + lineNumber + " lines.");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Helper methods to parse fields safely
    private Integer parseInteger(String value, int lineNumber) {
        try {
            return value.isEmpty() ? null : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer at line " + lineNumber + ": " + value);
            return null;
        }
    }

    private Double parseDouble(String value, int lineNumber) {
        try {
            return value.isEmpty() ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid double at line " + lineNumber + ": " + value);
            return null;
        }
    }

    private Long parseLong(String value, int lineNumber) {
        try {
            return value.isEmpty() ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid long at line " + lineNumber + ": " + value);
            return null;
        }
    }
}
