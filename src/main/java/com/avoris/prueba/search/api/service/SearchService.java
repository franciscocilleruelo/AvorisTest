package com.avoris.prueba.search.api.service;

import org.springframework.lang.NonNull;

import com.avoris.prueba.search.api.model.Search;

/**
 * Service interface for handling hotel search operations.
 * <p>
 * This interface defines the contract for performing hotel search operations.
 * It provides a method to process a hotel search based on the search request
 * and returns a unique search ID for tracking the search.
 * </p>
 */
public interface SearchService {

    /**
     * Performs a search based on the provided {@link Search} object.
     * <p>
     * The search request contains details such as hotel ID, check-in and check-out dates,
     * and the ages of people in the search. This method processes the search and returns
     * a unique ID that identifies the search request.
     * </p>
     * 
     * @param search the search request containing the details of the hotel search. Must not be null.
     * @return a unique search ID associated with the search.
     */
    String search(@NonNull Search search);

}
