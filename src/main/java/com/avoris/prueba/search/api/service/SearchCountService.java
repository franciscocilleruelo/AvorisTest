package com.avoris.prueba.search.api.service;

import java.util.Optional;

import com.avoris.prueba.search.api.jpa.model.SearchCount;
import com.avoris.prueba.search.api.response.SearchCountResponseDTO;

/**
 * Service interface for handling search count operations.
 * Provides methods to retrieve search count details by search ID.
 */
public interface SearchCountService {

    /**
     * Retrieves the {@link SearchCountResponseDTO} for a given search ID.
     * 
     * @param searchId the unique ID of the search.
     * @return an {@link Optional} containing the {@link SearchCountResponseDTO}, or empty if no search is found.
     */
    public Optional<SearchCount> getSearchCountResponseBySearchId(String searchId);

}
