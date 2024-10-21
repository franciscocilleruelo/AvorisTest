package com.avoris.prueba.search.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Main class for the Search API application.
 * <p>
 * This class serves as the entry point for the Spring Boot application. 
 * It also enables retry functionality through the {@link EnableRetry} annotation, 
 * allowing certain operations to be retried automatically in case of failure.
 * </p>
 */
@SpringBootApplication
@EnableRetry // Enable retries
public class SearchApiApplication {

    /**
     * The main method to run the Search API application.
     * <p>
     * This method starts the Spring Boot application, initializing all necessary components
     * and configurations.
     * </p>
     * 
     * @param args command-line arguments passed during startup.
     */
    public static void main(String[] args) {
        SpringApplication.run(SearchApiApplication.class, args);
    }

}
