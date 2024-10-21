package com.avoris.prueba.search.api.messaging.event;

import java.util.Collections;
import java.util.List;

/**
 * Represents a search event that contains the details of a hotel search request.
 * <p>
 * This event object is used to transfer search data between services and is also
 * sent to Kafka topics to notify other services about a new hotel search request.
 * The event includes details such as the hotel ID, check-in and check-out dates,
 * the ages of the people in the search, and a unique search ID.
 * </p>
 */
public class SearchEvent {

    private String hotelId;
    private String checkIn;
    private String checkOut;
    private List<Integer> ages;
    private String searchId;

    /**
     * Default constructor for {@link SearchEvent}.
     * <p>
     * Initializes a new instance of {@link SearchEvent} without setting any values.
     * This is necessary for frameworks like Spring to instantiate this class.
     * </p>
     */
    public SearchEvent() {
    }

    /**
     * Constructs a {@link SearchEvent} with the given parameters.
     * 
     * @param hotelId the ID of the hotel being searched.
     * @param checkIn the check-in date for the hotel.
     * @param checkOut the check-out date for the hotel.
     * @param ages the list of ages of the people involved in the search.
     * @param searchId the unique ID associated with the search.
     */
    public SearchEvent(String hotelId, String checkIn, String checkOut, List<Integer> ages, String searchId) {
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.searchId = searchId;
        // Defensive copy to prevent external mutation of the ages list
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
     * Sets the hotel ID.
     * 
     * @param hotelId the ID of the hotel to set.
     */
    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
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
     * Sets the check-in date.
     * 
     * @param checkIn the check-in date to set.
     */
    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
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
     * Sets the check-out date.
     * 
     * @param checkOut the check-out date to set.
     */
    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    /**
     * Gets the list of ages associated with the search.
     * 
     * @return the list of ages.
     */
    public List<Integer> getAges() {
        return ages;
    }

    /**
     * Sets the list of ages.
     * 
     * @param ages the list of ages to set.
     */
    public void setAges(List<Integer> ages) {
        this.ages = ages;
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
     * Sets the unique search ID.
     * 
     * @param searchId the search ID to set.
     */
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
