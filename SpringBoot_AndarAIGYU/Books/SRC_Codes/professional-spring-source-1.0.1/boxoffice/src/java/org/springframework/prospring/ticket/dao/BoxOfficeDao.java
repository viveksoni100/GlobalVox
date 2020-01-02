package org.springframework.prospring.ticket.dao;

import java.math.*;
import java.util.*;

import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.domain.support.*;
import org.springframework.prospring.ticket.service.*;


/**
 * Interface defining all data access methods for the
 * "ticket" sample application.
 */
public interface BoxOfficeDao {

    /**
     * Marks the given seats for the given performance as reserved for the given booking.
     * @param seats The seats to mark as reserved.
     * @param performance The peformance for which the seats are reserved.
     * @param booking The booking for which the seats are reserved.
     * @throws RequestedSeatNotAvailableException Thrown if any of the seats are not available anymore, that is, they
     *         are already reserved.
     */
    public void reserveSeats(Seat[] seats, Performance performance, Booking booking) 
			throws RequestedSeatNotAvailableException;

    /**
     * Saves the purchase of the given booking in the database.
     * @param booking The booking of which the purchase will be saved in the database.
     */
    public void savePurchaseForBooking(Booking booking);

    /**
     * Returns all generes in the database.
     * @return All generes in the database.
     */
    public List getAllGenres();

    /**
     * Returns all generes with shows in the database
     * @return All generes with shows in the database
     */
    public List getCurrentGenres();

    /**
     * Returns the show associated with the given show id.
     * @param id The id of the requested show.
     * @return The show associated with the given show id.
     */
    public Show getShow(long id);

    /**
     * Returns the performance associated with the given performance id.
     * @param id The id of the requested performance.
     * @return The performance associated with the given performance id.
     */
	public Performance getPerformance(long id);

    /**
     * Returns the price band associated with the given price band id.
     * @param id The id of the requesed price band.
     * @return The price band associated with the given price band id.
     */
    public PriceBand getPriceBand(long id);

    /**
     * Returns the price structure associated with the given price structure id.
     * @param id The id of the requested price structure.
     * @return The price structure associated with the given price structure id.
     */
    public PriceStructure getPriceStructure(long id);

    /**
     * Returns a list of all performances of the given show. The returned performances hold information
     * about seats availability for each performance.
     * @param show The given show.
     * @return A list of all performances (with availability) of the given show.
     */
    public PerformanceWithAvailability[] getAvailabilityForPerformances(Show show);

    /**
     * Returns a list of all available seats of the given class in the given performance.
     * @param performance The given performance.
     * @param seatClass The class of the requested seats.
     * @return A list of all available seats of the given class in the given performance.
     */
    public Seat[] getAvailableSeats(Performance performance, SeatClass seatClass);

    /**
     * Returns the number of available seats of the given class in the given performance.
     * @param performance The given performance.
     * @param seatClass The class of the requested seats.
     * @return Returns the number of available seats of the given class in the given performance.
     */
    public int getAvailableSeatsCount(Performance performance, SeatClass seatClass);

    /**
     * Returns the cost of the given seats in the given performance.
     * @param performance The given performance.
     * @param seats The given seats.
     * @return The cost of the given seats in the given performance.
     */
    public BigDecimal getCostOfSeats(Performance performance, Seat[] seats);

    /**
     * Removes the given booking from the database.
     * @param booking The booking to be removed from the database.
     */
    public void removeBooking(Booking booking);

    /**
     * Returns the booking associated with the given id.
     * @param bookingId The id of the requested booking.
     * @return The booking associated with the given id.
     */
    public Booking getBooking(long bookingId);

    /**
     * Returns the seats associated with the given booking.
     * @param booking The given booking.
     * @return The seats associated with the given booking.
     */
    public List getSeatsForBooking(Booking booking);

    /**
     * Returns the performance associated with the given booking.
     * @param booking The given booking.
     * @return The performance associated with the given booking.
     */
    public Performance getPerformanceForBooking(Booking booking);

    /**
     * Checks the availability of the seats of the given booking.
     * @param booking The booking to check.
     * @return True if the seats are available, false otherwise.
     */
    public boolean checkAvailabilityForBooking(Booking booking);

    /**
     * Updates the authorization code of the given purchase in the database.
     * @param purchase The purchase to update.
     */
    public void updatePurchaseAuthorizationCode(Purchase purchase);
}
