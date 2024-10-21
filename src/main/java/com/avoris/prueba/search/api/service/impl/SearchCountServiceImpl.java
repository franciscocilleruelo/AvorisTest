package com.avoris.prueba.search.api.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.avoris.prueba.search.api.jpa.adapter.SearchRepositoryAdapter;
import com.avoris.prueba.search.api.jpa.model.SearchCount;
import com.avoris.prueba.search.api.model.Search;
import com.avoris.prueba.search.api.service.SearchCountService;

/**
 * Implementation of the {@link SearchCountService} interface.
 * <p>
 * This class handles the logic to retrieve search count details for a given search ID.
 * It interacts with the {@link SearchRepositoryAdapter} to find a search and count similar searches.
 * </p>
 */
@Service
public class SearchCountServiceImpl implements SearchCountService {

    private final SearchRepositoryAdapter searchRepositoryAdapter;

    /**
     * Constructs a {@link SearchCountServiceImpl} with the provided {@link SearchRepositoryAdapter}.
     * 
     * @param searchRepositoryAdapter the repository adapter used to retrieve search documents and count similar searches.
     */
    public SearchCountServiceImpl(SearchRepositoryAdapter searchRepositoryAdapter) {
        this.searchRepositoryAdapter = searchRepositoryAdapter;
    }

    /**
     * Retrieves the {@link SearchCount} for a given search ID.
     * <p>
     * This method finds the search document by the search ID and counts similar searches
     * based on the hotel ID, check-in and check-out dates, and ages.
     * </p>
     * 
     * @param searchId the unique ID of the search.
     * @return an {@link Optional} containing the {@link SearchCount}, or empty if no search is found.
     */
    @Override
    public Optional<SearchCount> getSearchCountResponseBySearchId(String searchId) {
        Optional<Search> searchDocumentOptional = searchRepositoryAdapter.findBySearchId(searchId);

        if (searchDocumentOptional.isEmpty()) {
            return Optional.empty();
        }

        Search search = searchDocumentOptional.get();

        long similarSearchCount = searchRepositoryAdapter.countSearches(
            search.getHotelId(),
            search.getCheckIn(),
            search.getCheckOut(),
            search.getAges()
        );

        SearchCount.SearchDetails searchDetails = new SearchCount.SearchDetails(
            search.getHotelId(),
            search.getCheckIn(),
            search.getCheckOut(),
            search.getAges()
        );

        return Optional.of(new SearchCount(searchId, searchDetails, similarSearchCount));
    }
}
