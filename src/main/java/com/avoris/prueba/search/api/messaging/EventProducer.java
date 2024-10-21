package com.avoris.prueba.search.api.messaging;

import com.avoris.prueba.search.api.messaging.event.SearchEvent;

/**
 * Interface for defining the contract of an event producer.
 * <p>
 * This interface defines the method for sending {@link SearchEvent} messages. 
 * Implementations of this interface are responsible for producing and sending messages 
 * to external systems or services (such as Kafka or other messaging systems).
 * </p>
 */
public interface EventProducer {

    /**
     * Sends a {@link SearchEvent} message to an external system (e.g., a messaging broker like Kafka).
     * <p>
     * This method will be implemented to handle the sending of events asynchronously.
     * </p>
     *
     * @param searchEvent the {@link SearchEvent} containing the details of the search that needs to be sent
     */
    void sendMessage(SearchEvent searchEvent);
}
