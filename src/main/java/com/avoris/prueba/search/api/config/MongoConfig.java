package com.avoris.prueba.search.api.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * Configuration class for setting up the MongoDB client.
 * <p>
 * This class provides configuration for connecting to MongoDB, 
 * including connection timeout, socket timeout, and connection pool size.
 * It creates and configures a {@link MongoClient} instance to be used
 * throughout the application for MongoDB operations.
 * </p>
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.mongodb.connection-timeout}")
    private int connectionTimeout;

    @Value("${spring.mongodb.socket-timeout}")
    private int socketTimeout;

    @Value("${spring.mongodb.pool.max-size}")
    private int maxPoolSize;
    
    /**
     * Default constructor for {@link MongoConfig}.
     * This class does not require any custom initialization.
     */
    public MongoConfig() {
        // No custom initialization required
    }

    /**
     * Configures and creates a {@link MongoClient} for MongoDB access.
     * <p>
     * This method uses the MongoDB URI, connection timeout, socket timeout, 
     * and connection pool size to set up the MongoDB client. 
     * It creates a {@link MongoClientSettings} object and applies the connection 
     * settings, then returns a {@link MongoClient} instance for interacting with MongoDB.
     * </p>
     * 
     * @return a configured {@link MongoClient} instance.
     */
    @Bean
    MongoClient mongoClient() {
        // Set up the connection string and Mongo client settings
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> 
                        builder.maxConnectionIdleTime(socketTimeout, TimeUnit.MILLISECONDS)
                               .maxWaitTime(connectionTimeout, TimeUnit.MILLISECONDS)
                               .maxSize(maxPoolSize))
                .build();
        return MongoClients.create(settings);
    }
}
