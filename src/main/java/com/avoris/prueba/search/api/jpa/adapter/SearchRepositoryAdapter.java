package com.avoris.prueba.search.api.jpa.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.avoris.prueba.search.api.jpa.repository.SearchJpaRepository;
import com.avoris.prueba.search.api.mapper.SearchMapper;
import com.avoris.prueba.search.api.model.Search;
import com.avoris.prueba.search.api.repository.SearchRepository;

/**
 * Adapter class that implements the {@link SearchRepository} port using JPA and performs
 * the necessary conversions between JPA entities and domain objects.
 * <p>
 * This adapter acts as a bridge between the core domain logic and the persistence layer
 * using Spring Data JPA for database operations.
 * </p>
 */
@Component
public class SearchRepositoryAdapter implements SearchRepository {
	
	private final SearchJpaRepository searchJpaRepository;
	private final SearchMapper searchMapper;

	/**
     * Constructor for {@link SearchRepositoryAdapter}, injecting the JPA repository and mapper.
     * 
     * @param searchJpaRepository the JPA repository used to perform database operations.
     * @param searchMapper the mapper used to convert between JPA entities and domain objects.
     */
	public SearchRepositoryAdapter(SearchJpaRepository searchJpaRepository, SearchMapper searchMapper) {
		super();
		this.searchJpaRepository = searchJpaRepository;
		this.searchMapper = searchMapper;
	}
	
	/**
     * Checks whether a search exists by its ID.
     * <p>
     * This method calls the JPA repository to check if a search with the given ID exists.
     * </p>
     * 
     * @param searchId the ID of the search.
     * @return {@code true} if the search exists, otherwise {@code false}.
     */
	@Override
	public boolean existsById(String searchId) {
		return searchJpaRepository.existsById(searchId);
	}

	/**
     * Finds a search by its ID and maps the result to a domain object.
     * <p>
     * This method retrieves a search entity by its ID, maps it to the {@link Search} domain object, 
     * and returns it wrapped in an {@link Optional}.
     * </p>
     * 
     * @param searchId the ID of the search.
     * @return an {@link Optional} containing the mapped {@link Search} domain object, or empty if not found.
     */
	@Override
	public Optional<Search> findBySearchId(String searchId) {
		return searchJpaRepository.findById(searchId)
	            .map(searchMapper::toDomain); 
	}

	/**
     * Counts the number of similar searches based on hotel ID, check-in and check-out dates, and guest ages.
     * <p>
     * This method calls a custom query in the JPA repository to count searches that match the provided criteria.
     * </p>
     * 
     * @param hotelId the ID of the hotel.
     * @param checkIn the check-in date.
     * @param checkOut the check-out date.
     * @param ages the list of guest ages.
     * @return the number of matching searches.
     */
	@Override
	public long countSearches(String hotelId, String checkIn, String checkOut, List<Integer> ages) {
		return searchJpaRepository.countByCustomQuery(hotelId, checkIn, checkOut, ages);
	}

}
