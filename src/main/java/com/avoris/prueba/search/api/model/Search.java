package com.avoris.prueba.search.api.model;

import java.util.Collections;
import java.util.List;

/**
 * Represents the search details for hotel availability.
 * <p>
 * This class is used to store and manage the search parameters for hotel availability searches,
 * including the hotel ID, check-in and check-out dates, and the ages of the people involved in the search.
 * </p>
 * <p>
 * This class acts as a domain object in the application, representing the core data needed for searches.
 * </p>
 */
public class Search {
    
	private String searchId;
    private String hotelId;
    private String checkIn;
    private String checkOut;
    private List<Integer> ages;

    /**
     * Default constructor for {@link Search}.
     * <p>
     * Initializes a new instance of {@link Search} without setting any values.
     * This is necessary for frameworks that require a no-arg constructor.
     * </p>
     */
    public Search() {
    }
    
    /**
     * Constructs a {@link Search} with the given search details.
     * 
     * @param searchId the ID of the searching.
     * @param hotelId the ID of the hotel being searched.
     * @param checkIn the check-in date for the hotel.
     * @param checkOut the check-out date for the hotel.
     * @param ages the list of ages of the people involved in the search.
     */
    public Search(String searchId, String hotelId, String checkIn, String checkOut, List<Integer> ages) {
    	this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        // Defensive copy to avoid external modification of the ages list
        this.ages = ages == null ? Collections.emptyList() : List.copyOf(ages);
    }
    
    /**
     * Gets the search ID.
     * 
     * @return the search ID.
     */
    public String getSearchId() {
		return searchId;
	}

    /**
     * Sets the search ID.
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
     * This method returns an unmodifiable list to ensure external code cannot modify the list.
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
     * Creates a defensive copy of the list to avoid external modification, ensuring data integrity.
     * </p>
     * 
     * @param ages the list of ages to set.
     */
    public void setAges(List<Integer> ages) {
        this.ages = ages == null ? Collections.emptyList() : List.copyOf(ages);
    }
}
