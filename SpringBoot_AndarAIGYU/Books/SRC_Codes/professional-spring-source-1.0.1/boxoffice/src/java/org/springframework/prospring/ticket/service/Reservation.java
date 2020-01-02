package org.springframework.prospring.ticket.service;

import org.springframework.prospring.ticket.domain.*;

/**
 * Represents a reservation in the system.
 */
public class Reservation {

    // the seats associated with this reservation.
    private Seat[] seats;

    // the booking associated with this reservation.
    private Booking booking;

    /**
     * Empty constructor to support javabean spec.
     */
    public Reservation() {
    }

    /**
     * Constructs a new reservation with given seats and booking.
	 * @param seats The seats associated with this reservation.
	 * @param booking The booking associated with this reservation.
	 */
	public Reservation(Seat[] seats, Booking booking) {
		this.seats = seats;
		this.booking = booking;
	}

    /**
     * Returns the booking of this reservation.
     * @return The booking of this reservation.
     */
    public Booking getBooking() {
		return booking;
	}

    /**
     * Sets the booking of this reservation.
     * @param booking The booking of this reservation.
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    /**
     * Returns the reserved seats of this reseravation.
     * @return The reserved seats of this reseravation.
     */
    public Seat[] getSeats() {
		return seats;
	}

    /**
     * Sets the seats of this reservation.
     * @param seats The seats of this reservation.
     */
    public void setSeats(Seat[] seats) {
        this.seats = seats;
    }

}
