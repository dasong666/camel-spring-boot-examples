package com.redhat.consulting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

	/**
	 * A main method to start this application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}