package com.trip.adviser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.web.client.*;

@SpringBootApplication
public class AdviserApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdviserApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
