package com.avoris.prueba.search.api.repository;

import java.util.List;
import java.util.Optional;

import com.avoris.prueba.search.api.model.Search;

/**
 * Repository interface for handling search operations.
 * <p>
 * This interface defines the methods for interacting with the search data, including checking
 * for existence, retrieving specific searches, and counting searches based on criteria.
 * The implementation will handle the connection to the underlying data source (e.g., a database).
 * </p>
 */
public interface SearchRepository {

    /**
     * Checks if a search exists by its unique ID.
     * 
     * @param searchId the unique identifier of the search to check.
     * @return {@code true} if the search exists, {@code false} otherwise.
     */
    boolean existsById(String searchId);

    /**
     * Finds a search by its unique search ID.
     * 
     * @param searchId the unique identifier of the search to find.
     * @return an {@link Optional} containing the {@link Search} if found, or {@code Optional.empty()} if not found.
     */
    Optional<Search> findBySearchId(String searchId);

    /**
     * Counts the number of searches that match the specified criteria.
     * <p>
     * The search is based on the hotel ID, check-in and check-out dates, and the list of ages.
     * </p>
     * 
     * @param hotelId the ID of the hotel involved in the search.
     * @param checkIn the check-in date for the search.
     * @param checkOut the check-out date for the search.
     * @param ages a list of ages of the people involved in the search.
     * @return the number of searches that match the given criteria.
     */
    long countSearches(String hotelId, String checkIn, String checkOut, List<Integer> ages);
}
