package com.avoris.prueba.search.api.jpa.model;

import java.util.Collections;
import java.util.List;

/**
 * Immutable response object representing the result of a search count operation.
 * <p>
 * This class contains the search ID, detailed information about the search, and the count of similar searches.
 * It is designed to be immutable to ensure thread safety and consistent behavior.
 * </p>
 */
public final class SearchCount {

    private final String searchId;
    private final SearchDetails search;
    private final long count;

    /**
     * Constructs a {@link SearchCount} with the given parameters.
     * 
     * @param searchId the unique ID of the search.
     * @param search the detailed information of the search.
     * @param count the number of similar searches found.
     */
    public SearchCount(String searchId, SearchDetails search, long count) {
        this.searchId = searchId;
        this.search = search;
        this.count = count;
    }

    /**
     * Gets the unique search ID.
     * 
     * @return the search ID.
     */
    public String getSearchId() {
        return searchId;
    }

    /**
     * Gets the detailed information of the search.
     * 
     * @return the {@link SearchDetails} object containing the search details.
     */
    public SearchDetails getSearch() {
        return search;
    }

    /**
     * Gets the count of similar searches found.
     * 
     * @return the count of similar searches.
     */
    public long getCount() {
        return count;
    }

    /**
     * Represents detailed information about a search, including hotel ID, check-in and check-out dates, and ages.
     * <p>
     * This inner static class encapsulates the specific details of a search, making it easier to pass around 
     * structured search-related data.
     * </p>
     */
    public static class SearchDetails {
        private final String hotelId;
        private final String checkIn;
        private final String checkOut;
        private final List<Integer> ages;

        /**
         * Constructs a {@link SearchDetails} object with the given parameters.
         * <p>
         * The list of ages is copied to ensure immutability. If the provided list is null, an empty list is used.
         * </p>
         * 
         * @param hotelId the ID of the hotel involved in the search.
         * @param checkIn the check-in date for the hotel.
         * @param checkOut the check-out date for the hotel.
         * @param ages the list of ages of the people in the search. If null, an empty list will be used.
         */
        public SearchDetails(String hotelId, String checkIn, String checkOut, List<Integer> ages) {
            this.hotelId = hotelId;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
            // Defensive copy to avoid external modification of the ages list
            this.ages = ages == null ? Collections.emptyList() : List.copyOf(ages);
        }

        /**
         * Gets the hotel ID.
         * 
         * @return the hotel ID.
         */
        public String getHotelId() {
            return hotelId;
        }

        /**
         * Gets the check-in date.
         * 
         * @return the check-in date.
         */
        public String getCheckIn() {
            return checkIn;
        }

        /**
         * Gets the check-out date.
         * 
         * @return the check-out date.
         */
        public String getCheckOut() {
            return checkOut;
        }

        /**
         * Gets the list of ages associated with the search.
         * <p>
         * The list is returned as an unmodifiable list to prevent external modification, ensuring immutability.
         * </p>
         * 
         * @return an unmodifiable list of ages.
         */
        public List<Integer> getAges() {
            return Collections.unmodifiableList(ages);
        }
    }
}
