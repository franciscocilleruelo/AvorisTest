package com.avoris.prueba.search.api.jpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.avoris.prueba.search.api.jpa.model.SearchDocument;

/**
 * Repository interface for performing CRUD operations on {@link SearchDocument} objects in MongoDB.
 * <p>
 * This interface extends {@link MongoRepository} to provide standard MongoDB data access methods, 
 * including custom queries for counting search documents based on specific search criteria.
 * </p>
 */
public interface SearchJpaRepository extends MongoRepository<SearchDocument, String> {

    /**
     * Finds a {@link SearchDocument} by its unique search ID.
     * <p>
     * This method retrieves a search document from MongoDB based on the provided search ID. 
     * If no document is found, it returns an empty {@link Optional}.
     * </p>
     * 
     * @param searchId the search ID to search for.
     * @return an {@link Optional} containing the found {@link SearchDocument}, or empty if not found.
     */
    Optional<SearchDocument> findBySearchId(String searchId);

    /**
     * Counts the number of search documents matching the specified hotel ID, check-in and check-out dates, and list of ages.
     * <p>
     * This query uses a custom MongoDB query with the {@code @Query} annotation. It filters the search documents 
     * based on the given parameters and returns the number of documents that match the criteria.
     * </p>
     * 
     * @param hotelId the ID of the hotel.
     * @param checkIn the check-in date.
     * @param checkOut the check-out date.
     * @param ages the list of ages of the people in the search.
     * @return the number of matching search documents.
     */
    @Query(value = "{ 'hotelId': ?0, 'checkIn': ?1, 'checkOut': ?2, 'ages': ?3 }", count = true)
    long countByCustomQuery(String hotelId, String checkIn, String checkOut, List<Integer> ages);
}
