package com.avoris.prueba.search.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.avoris.prueba.search.api.jpa.model.SearchCount;
import com.avoris.prueba.search.api.jpa.model.SearchDocument;
import com.avoris.prueba.search.api.model.Search;
import com.avoris.prueba.search.api.request.SearchRequestDTO;
import com.avoris.prueba.search.api.response.SearchCountResponseDTO;

/**
 * Mapper interface for converting between {@link SearchDocument}, {@link Search} (domain), 
 * and {@link SearchCountResponseDTO}.
 * <p>
 * This mapper facilitates the conversion between different layers of the application:
 * - The persistence layer ({@link SearchDocument} - JPA entity).
 * - The domain layer ({@link Search} - core business logic).
 * - The presentation layer ({@link SearchCountResponseDTO} - DTO for API responses).
 * </p>
 * <p>
 * The interface is implemented automatically by MapStruct at compile time, generating
 * the necessary boilerplate code for mapping between different object types.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface SearchMapper {

    /**
     * An instance of the {@link SearchMapper} created by MapStruct.
     * This instance is used to invoke the mapping methods.
     */
    SearchMapper INSTANCE = Mappers.getMapper(SearchMapper.class);

    /**
     * Converts a {@link SearchDocument} (JPA entity) to a {@link Search} (domain object).
     * <p>
     * This method is responsible for converting a persistence-layer entity (used for storing
     * data in the database) to a domain object (used in the business logic layer).
     * </p>
     *
     * @param entity the {@code SearchDocument} to be converted
     * @return the corresponding {@code Search} domain object
     */
    Search toDomain(SearchDocument entity);
    
    /**
     * Converts a {@link SearchRequestDTO} (DTO for API request) to a {@link Search} (domain object).
     * <p>
     * This method is used to map data from a request DTO, which is typically received from an API request,
     * to a domain object that can be processed by the business logic layer.
     * </p>
     *
     * @param searchRequest the {@code SearchRequestDTO} to be converted
     * @return the corresponding {@code Search} domain object
     */
    Search toDomain(SearchRequestDTO searchRequest);

    /**
     * Converts a {@link Search} (domain object) to a {@link SearchDocument} (JPA entity).
     * <p>
     * This method converts a domain object to a persistence-layer entity, allowing it to be
     * stored in the database.
     * </p>
     *
     * @param search the {@code Search} domain object to be converted
     * @return the corresponding {@code SearchDocument} JPA entity
     */
    SearchDocument toEntity(Search search);

    /**
     * Converts a {@link SearchCount} (domain object) to a {@link SearchCountResponseDTO} (DTO for API response).
     * <p>
     * This method is used to convert a domain object representing the search count data into a response DTO
     * that is returned by the API.
     * </p>
     *
     * @param searchCount the {@code SearchCount} domain object to be converted
     * @return the corresponding {@code SearchCountResponseDTO} DTO for the API response
     */
    SearchCountResponseDTO toDTO(SearchCount searchCount);
}
