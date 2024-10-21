package com.avoris.prueba.search.api.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.avoris.prueba.search.api.jpa.model.SearchDocument;
import com.avoris.prueba.search.api.jpa.repository.SearchJpaRepository;
import com.avoris.prueba.search.api.messaging.event.SearchEvent;

/**
 * Kafka consumer service for handling and processing messages from Kafka topics.
 * <p>
 * This service listens to the Kafka topic for {@link SearchEvent} messages and 
 * processes them by saving the received search details into a MongoDB database.
 * </p>
 */
@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    
    private final SearchJpaRepository searchRepository;
    
    /**
     * Constructor for {@link KafkaConsumer}, injecting the {@link SearchJpaRepository}.
     * <p>
     * The repository is used to persist search data that is received from the Kafka topic.
     * </p>
     * 
     * @param searchRepository the repository for persisting the {@link SearchDocument} data.
     */
    public KafkaConsumer(SearchJpaRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    /**
     * Consumes {@link SearchEvent} messages from the configured Kafka topic and 
     * saves the search details into the database.
     * <p>
     * This method is annotated with {@link KafkaListener}, which subscribes to the topic 
     * defined in the application's configuration. Upon receiving a message, the data 
     * is converted to a {@link SearchDocument} and stored in MongoDB.
     * </p>
     * 
     * @param searchEvent the search event received from the Kafka topic.
     */
    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(SearchEvent searchEvent) {
        logger.info("Mensaje recibido desde Kafka: {}", searchEvent);

        // Create a SearchDocument from the received SearchEvent and persist it in the database
        SearchDocument searchDocument = new SearchDocument();
        searchDocument.setSearchId(searchEvent.getSearchId());
        searchDocument.setHotelId(searchEvent.getHotelId());
        searchDocument.setCheckIn(searchEvent.getCheckIn());
        searchDocument.setCheckOut(searchEvent.getCheckOut());
        searchDocument.setAges(searchEvent.getAges());
        
        searchRepository.save(searchDocument);
    }
}
