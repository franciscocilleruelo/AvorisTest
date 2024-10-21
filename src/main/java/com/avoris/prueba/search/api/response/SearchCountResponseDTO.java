package com.avoris.prueba.search.api.response;

import java.util.List;

/**
 * Immutable response object representing the result of a search count operation.
 * <p>
 * This object contains the unique search ID, detailed information of the search, and
 * the count of similar searches that match the search criteria.
 * </p>
 * <p>
 * This class is immutable to ensure that the state of a search response cannot be modified
 * once it has been created.
 * </p>
 */
public record SearchCountResponseDTO(
        String searchId,
        SearchDetails search,
        long count
) {

    /**
     * Represents detailed information about a search, including hotel ID, check-in and check-out dates, and ages.
     * <p>
     * This inner static class ensures immutability by providing final fields and defensive copying for collections.
     * </p>
     */
    public record SearchDetails(
            String hotelId,
            String checkIn,
            String checkOut,
            List<Integer> ages
    ) {

    }
}
