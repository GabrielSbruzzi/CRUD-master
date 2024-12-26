package com.crud.gabriel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.crud.gabriel.repository")
public class GabrielApplication {

	public static void main(String[] args) {
		SpringApplication.run(GabrielApplication.class, args);
	}
}
