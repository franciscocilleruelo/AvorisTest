package com.avoris.prueba.search.api.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration class for the web server and task executor.
 * <p>
 * This class customizes the web server port and configures a thread pool task executor 
 * for handling asynchronous tasks. It provides beans for setting up both the web server 
 * configuration (such as the port) and the task execution pool (core size, max size, etc.).
 * </p>
 */
@Configuration
public class WebServerConfig {

    @Value("${server.port}")
    private int port;

    @Value("${spring.task.execution.pool.core-size}")
    private int corePoolSize;

    @Value("${spring.task.execution.pool.max-size}")
    private int maxPoolSize;

    @Value("${spring.task.execution.pool.queue-capacity}")
    private int queueCapacity;
    
    /**
     * Default constructor for {@link WebServerConfig}.
     * <p>
     * This class does not require any custom initialization.
     * </p>
     */
    public WebServerConfig() {
        // No custom initialization required
    }

    /**
     * Configures the task executor using a thread pool.
     * <p>
     * This method sets up a {@link ThreadPoolTaskExecutor} with a configurable core pool size, 
     * maximum pool size, and queue capacity for handling high concurrency tasks.
     * </p>
     * 
     * @return a configured {@link Executor} instance with core pool size, max pool size, and queue capacity.
     */
    @Bean
    Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("HighConcurrencyThread-");
        executor.initialize();
        return executor;
    }

    /**
     * Customizes the web server factory to set the server port.
     * <p>
     * This method creates a {@link WebServerFactoryCustomizer} bean that sets the web server 
     * port based on the value defined in the `server.port` property.
     * </p>
     * 
     * @return a {@link WebServerFactoryCustomizer} that configures the port for the web server.
     */
    @Bean
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setPort(port);
    }
}
