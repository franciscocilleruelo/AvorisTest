package com.avoris.prueba.search.api.response;

/**
 * Immutable response object representing the result of a hotel search operation.
 * <p>
 * This record contains the unique search ID associated with a hotel search request.
 * It is used to return the search ID to the client after a successful search request.
 * </p>
 * <p>
 * The record is designed to be immutable, ensuring that the state of the object
 * cannot be modified after it is created.
 * </p>
 */
public record SearchResponseDTO(String searchId) {

    /**
     * Constructs a {@link SearchResponseDTO} with the given search ID.
     * <p>
     * This constructor initializes the record with the unique search ID, which is typically
     * generated by the system when a search is created.
     * </p>
     * 
     * @param searchId the unique ID associated with the hotel search.
     */
    public SearchResponseDTO {
        // The searchId is automatically set in a record.
    }
}
