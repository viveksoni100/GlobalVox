package org.springframework.prospring.ticket.domain;

import java.io.*;

/**
 * Represents the status (available, booked, etc...) of a seat within a performance.
 */
public class SeatStatus implements Serializable {

    // the associated seat.
    private Seat seat;

    // the price band of the assoicated seat.
    private PriceBand priceBand;

    // the associated performance.
    private Performance performance;

    // the booking associated with this seat status
    private Booking booking;

    /**
     * Empty constructor to support javabean spec.
     */
    public SeatStatus() {
    }

    /**
     * Constructs a new seat status with given seat, price band, and peformance.
     * @param seat The seat associated with this seat status.
     * @param priceBand The price band associated with this seat status.
     * @param performance The performance associated with this seat status.
     */
    public SeatStatus(Seat seat, PriceBand priceBand, Performance performance) {
        this.seat = seat;
        this.priceBand = priceBand;
        this.performance = performance;
    }

    /**
     * Returns the seat that is associated with this seat status.
     * @return The seat that is associated with this seat status.
     */
    public Seat getSeat() {
        return seat;
    }

    /**
     * Sets the seat that is associated with this seat status.
     * @param seat The seat that is associated with this seat status.
     */
    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    /**
     * The price band that is associated with this seat status.
     * @return The price band that is associated with this seat status.
     */
    public PriceBand getPriceBand() {
        return priceBand;
    }

    /**
     * Sets the price band that is associated with this seat status.
     * @param priceBand The price band that is associated with this seat status.
     */
    public void setPriceBand(PriceBand priceBand) {
        this.priceBand = priceBand;
    }

    /**
     * Returns the performance this seat status is associated with.
     * @return The performance this seat status is associated with.
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Sets the performance this seat status is associated with.
     * @param performance The performance this seat status is associated with.
     */
    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    /**
     * Returns the booking for the seat that is associated with this seat status (if one exists).
     * @return The booking for the seat that is associated with this seat status. Null if no booking exists.
     */
    public Booking getBooking() {
        return booking;
    }

    /**
     * Sets the booking for the seat that is associated with this seat status.
     * @param booking The booking for the seat that is associated with this seat status.
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SeatStatus seatStatus = (SeatStatus) o;

        if (performance != null ? performance.getId() != seatStatus.performance.getId() : seatStatus.performance != null) {
            return false;
        }

        return (seat != null) ? seat.getId() == seatStatus.seat.getId() : seatStatus.seat == null;
    }

    public int hashCode() {
        int hashcode = (seat != null ? (int)seat.getId() : 0);
        hashcode = 31 * hashcode + (performance != null ? (int)performance.getId() : 0);
        return hashcode;
    }

}
