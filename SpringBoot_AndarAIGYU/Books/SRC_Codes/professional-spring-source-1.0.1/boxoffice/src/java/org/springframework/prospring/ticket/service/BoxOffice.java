package org.springframework.prospring.ticket.service;

import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.domain.support.*;
import org.springframework.prospring.ticket.service.payment.*;

/**
 * Represetns the Box Office in the system. Provides booking services which includes:<br/>
 *
 * - querying for available seats<br/>
 * - making reservations<br/>
 * - managing payments<br/>
 * - any other services that are related to the booking process.
 */
public interface BoxOffice {

    /**
     * Returns a list of performances with available seats information for a given show.
     * @param show The given show.
     * @return A list of performances with available seats information for a given show.
     */
    public PerformanceWithAvailability[] getAvailabilityForPerformances(Show show);

    /**
     * Allocates seats for a performance. A reservation with a booking is created based on the given
     * reservation request. The booking is reserved for the user for some time to allow the user to complete
     * the payment process.
     * @param request The given reservation request.
     * @return The newly created reservation.
     * @throws RequestedSeatNotAvailableException Thrown when requested sits are not available.
     * @throws NotEnoughSeatsException Thrown when there aren't enough sits to satisfy the given reuqest.
     */
    public Reservation allocateSeats(ReservationRequest request)
            throws RequestedSeatNotAvailableException, NotEnoughSeatsException;

    /**
     * Performs the tickets purchase based on the given purchase request.
     *
     * @param purchaseRequest The given purchase request.
     * @return The successful purchase that was made.
     * @throws PaymentException Thorwn if encountered any errors during the payment process.
     * @throws RequestedSeatNotAvailableException Thrown if the requested seats were not
     * available anymore at the time of the purchase.
     */
    public Purchase purchaseTickets(PurchaseRequest purchaseRequest)
        throws PaymentException, CreditCardValidationException, RequestedSeatNotAvailableException;

    /**
     * Check whether the seats associated with the given booking are still available.
     * @param booking The given booking.
     * @return True if the seats of the booking are still availabe, false otherwise.
     */
    public boolean areSeatsStillAvailable(Booking booking);

    /**
     * Returns the available seats of the given seat class in the given performance.
     * @param performance The given performance.
     * @param seatClass The class of the requested seats.
     * @return The available seats of the given seat class in the given performance.
     */
    public Seat[] getAvailableSeats(Performance performance, SeatClass seatClass);

    /**
     * Returns the number of available seats of the given seat class in the given performance.
     * @param performance The given performance.
     * @param seatClass The given seat class.
     * @return The number of available seats of the given seat class in the given performance.
     */
    public int getAvailableSeatsCount(Performance performance, SeatClass seatClass);

    /**
     * Returns the price band associated with the given id.
     * @param priceBandId The id of the requested price band.
     * @return The price band associated with the given id.
     */
    public PriceBand getPriceBand(long priceBandId);

    /**
     * Cancels the given booking.
     * @param booking The booking to be canceled.
     */
    public void cancelBooking(Booking booking);

    /**
     * Returns the booking identified by the given id.
     * @param bookingId The id of the requested booking.
     * @return The booking identified by the given id.
     */
    public Booking getBooking(long bookingId);

    /**
     * Returns the booked seats of the given booking.
     * @param booking The given booking.
     * @return The booked seats of the given booking.
     */
    public Seat[] getBookedSeats(Booking booking);

    /**
     * Returns the performance of the given booking.
     * @param booking The given booking.
     * @return The performance of the given booking.
     */
    public Performance getPerformanceOfBooking(Booking booking);
}