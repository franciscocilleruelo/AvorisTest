package com.avoris.prueba.search.api.messaging.adapter;

import org.springframework.stereotype.Component;

import com.avoris.prueba.search.api.kafka.KafkaProducer;
import com.avoris.prueba.search.api.messaging.EventProducer;
import com.avoris.prueba.search.api.messaging.event.SearchEvent;

/**
 * Adapter class that implements the {@link EventProducer} interface to send messages via Kafka.
 * <p>
 * This class acts as an adapter for the {@link KafkaProducer}, allowing the system to send
 * {@link SearchEvent} messages through Kafka by implementing the common {@link EventProducer} interface.
 * </p>
 */
@Component
public class KafkaProducerAdapter implements EventProducer {
	
    private final KafkaProducer kafkaProducer;

    /**
     * Constructor for {@link KafkaProducerAdapter}, injecting the {@link KafkaProducer}.
     * 
     * @param kafkaProducer the producer responsible for sending messages to Kafka.
     */
    public KafkaProducerAdapter(KafkaProducer kafkaProducer) {
        super();
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Sends a {@link SearchEvent} message to Kafka by delegating to the {@link KafkaProducer}.
     * <p>
     * This method uses the injected Kafka producer to send the message asynchronously.
     * </p>
     *
     * @param searchEvent the event containing the details of the search to be sent to Kafka.
     */
    @Override
    public void sendMessage(SearchEvent searchEvent) {
        kafkaProducer.sendMessage(searchEvent);
    }
}
