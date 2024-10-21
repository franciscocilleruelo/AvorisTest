package com.avoris.prueba.search.api.jpa.model;

import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a document for storing search details in MongoDB.
 * <p>
 * This class is used to persist and retrieve search data for hotel availability searches.
 * Each instance corresponds to a search performed by a user, and it includes details
 * like the hotel ID, check-in and check-out dates, ages of the guests, and a unique search ID.
 * </p>
 */
@Document(collection = "Searches")
public class SearchDocument {
    
    @Id
    private String searchId;
    private String hotelId;
    private String checkIn;
    private String checkOut;
    private List<Integer> ages;

    /**
     * Default constructor for {@link SearchDocument}.
     * <p>
     * Initializes an empty {@link SearchDocument}. This is primarily used by
     * data-binding frameworks like Spring when populating the object.
     * </p>
     */
    public SearchDocument() {
    }
    
    /**
     * Constructs a {@link SearchDocument} with the given search details.
     * <p>
     * The constructor initializes the search document with the specified hotel ID, check-in and check-out dates,
     * list of ages, and search ID. The list of ages is defensively copied to ensure immutability.
     * </p>
     * 
     * @param hotelId the ID of the hotel being searched.
     * @param checkIn the check-in date for the hotel.
     * @param checkOut the check-out date for the hotel.
     * @param ages the list of ages of the people associated with the search.
     * @param searchId the unique ID associated with the search.
     */
    public SearchDocument(String hotelId, String checkIn, String checkOut, List<Integer> ages, String searchId) {
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        // Defensive copy to avoid external modification of the ages list
        this.ages = ages == null ? Collections.emptyList() : List.copyOf(ages);
        this.searchId = searchId;
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
     * @param hotelId the hotel ID to set.
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
     * <p>
     * The list is returned as an unmodifiable list to prevent external modification, ensuring immutability.
     * </p>
     * 
     * @return an unmodifiable list of ages.
     */
    public List<Integer> getAges() {
        return Collections.unmodifiableList(ages);
    }

    /**
     * Sets the list of ages associated with the search.
     * <p>
     * The list is defensively copied to ensure immutability, preventing external modification.
     * If the provided list is null, an empty list is used.
     * </p>
     * 
     * @param ages the list of ages to set.
     */
    public void setAges(List<Integer> ages) {
        this.ages = ages == null ? Collections.emptyList() : List.copyOf(ages);
    }
}
