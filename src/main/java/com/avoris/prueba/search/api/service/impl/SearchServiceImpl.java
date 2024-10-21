package com.avoris.prueba.search.api.service.impl;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.avoris.prueba.search.api.jpa.adapter.SearchRepositoryAdapter;
import com.avoris.prueba.search.api.messaging.EventProducer;
import com.avoris.prueba.search.api.messaging.event.SearchEvent;
import com.avoris.prueba.search.api.model.Search;
import com.avoris.prueba.search.api.service.SearchService;

/**
 * Implementation of the {@link SearchService} interface.
 * <p>
 * This class handles hotel search operations, ensuring each search is uniquely identified
 * and that search details are sent to Kafka for further processing.
 * </p>
 * <p>
 * The service performs the search by generating a unique search ID, verifying that the ID
 * is not already in use, and then sending the search event details to a Kafka topic via
 * the {@link EventProducer}.
 * </p>
 */
@Service
public class SearchServiceImpl implements SearchService {
	
    private final SearchRepositoryAdapter searchRepositoryAdapter;
    private final EventProducer eventProducer;
    
    /**
     * Constructs a {@link SearchServiceImpl} with the provided {@link SearchRepositoryAdapter} and {@link EventProducer}.
     * 
     * @param searchRepositoryAdapter the repository adapter used to check for existing search IDs and manage persistence.
     * @param eventProducer the event producer used to send search events to Kafka.
     */
    public SearchServiceImpl(SearchRepositoryAdapter searchRepositoryAdapter, EventProducer eventProducer) {
        this.searchRepositoryAdapter = searchRepositoryAdapter;
        this.eventProducer = eventProducer;
    }

    /**
     * Performs a search based on the provided {@link Search} object.
     * <p>
     * This method generates a unique search ID, ensures the ID does not already exist,
     * and sends the search details to a Kafka topic for further processing.
     * </p>
     * 
     * @param search the {@code Search} object containing the details of the hotel search. Must not be null.
     * @return a unique search ID associated with the search.
     */
    @Override
    public String search(@NonNull Search search) {
        // Generate a random UUID for the search ID
        String searchId = UUID.randomUUID().toString();
        
        // Ensure the generated search ID is unique
        while (searchRepositoryAdapter.existsById(searchId)) {
            searchId = UUID.randomUUID().toString(); // Regenerate ID if it already exists
        }

        // Build the search event to be sent to Kafka
        SearchEvent searchEvent = new SearchEvent(
            search.getHotelId(),
            search.getCheckIn(),
            search.getCheckOut(),
            search.getAges(),
            searchId
        );
        
        // Send the search event to Kafka
        eventProducer.sendMessage(searchEvent);
        
        // Return the unique search ID
        return searchId;
    }

}
