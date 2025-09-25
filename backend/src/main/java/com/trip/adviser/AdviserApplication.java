package com.trip.adviser;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.web.client.*;

import com.trip.adviser.service.*;

@SpringBootApplication
public class AdviserApplication implements CommandLineRunner {

	@Autowired
	private AllCountriesService geonameService;

	public static void main(String[] args) {
		SpringApplication.run(AdviserApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
		// Update with the path to your allCountries.txt file
//		String filePath = "/Users/ybutko/Documents/Projects/trafficLabel/TripAdviser/backend/src/main/resources/AD.txt";
//		geonameService.loadGeonamesFromFile(filePath);
	}
}
