package com.avoris.prueba.search.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 * <p>
 * This class configures the OpenAPI documentation for the Hotel Search API service, 
 * providing metadata such as the title, version, and contact information for the API.
 * The generated documentation is accessible via Swagger UI, allowing developers to 
 * explore and interact with the API endpoints.
 * </p>
 */
@Configuration
public class SwaggerConfig {
	
	/**
     * Default constructor for {@link SwaggerConfig}.
     * <p>
     * This class does not require any custom initialization.
     * </p>
     */
    public SwaggerConfig() {
        // No custom initialization required
    }

    /**
     * Creates and configures a custom {@link OpenAPI} bean for the API documentation.
     * <p>
     * This method sets up the basic information for the API, including the title, version,
     * description, and contact information. The resulting {@link OpenAPI} instance is used 
     * to generate interactive API documentation via Swagger UI.
     * </p>
     * 
     * @return a configured {@link OpenAPI} instance containing metadata like title, version, and contact info.
     */
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Búsqueda de Hoteles")
                .version("1.0")
                .contact(new Contact().name("Francisco Cilleruelo").email("francisco.cilleruelo@gmail.com"))
                .description("Documentación del servicio API REST para la búsqueda de hoteles"));
    }
}
