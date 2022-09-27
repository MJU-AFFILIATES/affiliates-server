package com.example.affiliates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AffiliatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AffiliatesApplication.class, args);
	}

}
