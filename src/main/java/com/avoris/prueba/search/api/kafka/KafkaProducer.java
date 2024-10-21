package com.avoris.prueba.search.api.kafka;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.avoris.prueba.search.api.messaging.event.SearchEvent;

/**
 * Kafka producer service for sending messages to Kafka topics.
 * <p>
 * This service is responsible for producing and sending {@link SearchEvent} messages to a specified Kafka topic. 
 * It supports retry mechanisms and recovery logic in case of message send failures.
 * </p>
 */
@Service
public class KafkaProducer {
	
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, SearchEvent> kafkaTemplate;
    
    @Value("${app.kafka.topic}") 
    private String topic;

    /**
     * Constructor for {@link KafkaProducer}, injecting the {@link KafkaTemplate}.
     * <p>
     * The KafkaTemplate is used to send serialized {@link SearchEvent} objects to the Kafka topic asynchronously.
     * </p>
     * 
     * @param kafkaTemplate the template used to send {@link SearchEvent} messages to Kafka.
     */
    public KafkaProducer(KafkaTemplate<String, SearchEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a {@link SearchEvent} message to the configured Kafka topic.
     * <p>
     * The message is sent asynchronously using a {@link CompletableFuture} to handle success and failure cases.
     * This method is annotated with {@link Retryable}, meaning it will retry sending the message 
     * in case of failure, with a maximum of 3 attempts and a 2-second backoff period.
     * </p>
     * 
     * @param searchEvent the event containing search details to be sent to Kafka.
     */
    @Retryable(
    	    retryFor = { Exception.class }, // Retry for any exception
    	    maxAttempts = 3,                // Maximum number of attempts
    	    backoff = @Backoff(delay = 2000) // Wait for 2 seconds between retries
    	)
    public void sendMessage(SearchEvent searchEvent) {
        CompletableFuture<SendResult<String, SearchEvent>> future = kafkaTemplate.send(topic, searchEvent);

        // Handle success and error asynchronously
        future.thenAccept(result -> logger.info("Mensaje enviado al topic: {}", result.getRecordMetadata().topic()))
              .exceptionally(ex -> {
                  logger.error("Error al enviar el mensaje: {}", ex.getMessage());
                  return null;
              });
    }
    
    /**
     * Recovery method called after retry attempts have been exhausted.
     * <p>
     * This method is invoked when all retry attempts to send the message to Kafka fail. 
     * It logs the failure and the exception that caused the failure, providing a mechanism to handle 
     * failed messages gracefully.
     * </p>
     *
     * @param ex the exception that was thrown during the message send attempt
     * @param searchEvent the {@link SearchEvent} that could not be sent to Kafka
     */
    @Recover
    public void recover(Exception ex, SearchEvent searchEvent) {
        logger.error("No se pudo enviar el mensaje a Kafka después de varios intentos: {}. La excepción generada es: {}", searchEvent, ex.getMessage());
    }
}
