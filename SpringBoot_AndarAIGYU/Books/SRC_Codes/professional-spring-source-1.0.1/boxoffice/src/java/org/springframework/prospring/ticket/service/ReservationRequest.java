package org.springframework.prospring.ticket.service;

import java.math.*;
import java.util.*;

import org.springframework.prospring.ticket.domain.*;

/**
 * Represents a reservation request. This is a request to reserve a number of seats for
 * a specific performance.
 */
public class ReservationRequest {

    private static final long MILLIS_PER_MINUTE = 1000L * 60L;

    // the related performance.
    private Performance performance;

    // the number of seats requested.
    private int numberOfSeatsRequested;

    // the price band of the requested seats.
    private PriceBand priceBand;

    // the date till which the seats should be reserved for the reservation.
    private Date holdUntil;

    // the number of minutes the reservation is held.
    private int minutesToHold;

    // the booking fee for the reservation.
    private BigDecimal bookingFee = new BigDecimal(0);

    /**
     * Empty constructor to support javabeans spec.
     */
    public ReservationRequest() {
    }

    /**
     * Constructs a new reservation request.
     * @param performance The performance the request is related to.
     * @param priceBand the price band of the requested seats.
     * @param numberOfSeatsRequested The number of the requested seats.
     * @param minutesToHold The number of minutes to reserve the seats.
     */
    public ReservationRequest(
        Performance performance,
        PriceBand priceBand,
        int numberOfSeatsRequested,
        int minutesToHold) {

        this.performance = performance;
        this.priceBand = priceBand;
        this.numberOfSeatsRequested = numberOfSeatsRequested;
        holdFor(minutesToHold);
    }

    /**
     * Returns the date till which the seats will be reserved while the purchase is not made.
     * @return The date till which the seats will be reserved while the purchase is not made.
     */
    public Date getHoldUntil() {
        return holdUntil;
    }

    /**
     * Returns the number of minutes the seats are reserved while the purchase is not made.
     * @return The number of minutes the seats are reserved while the purchase is not made.
     */
    public int getMinutesToHold() {
        return minutesToHold;
    }

    /**
     * Sets the number of minutes the seats will be reserved while the purchase is not made.
     * @param minutes The number of minutes the seats will be reserved while the purchase is not made.
     */
    public void holdFor(int minutes) {
        minutesToHold = minutes;
        holdUntil = new Date(System.currentTimeMillis() + minutes * MILLIS_PER_MINUTE);
    }

    /**
     * Returns the related performance of this reqeust.
     * @return The related performance of this reqeust.
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Sets the related performance of this reqeust.
     * @param performance The related performance of this request.
     */
    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    /**
     * Returns the number of seats reqeusted.
     * @return The number of seats reqeusted.
     */
    public int getNumberOfSeatsRequested() {
        return numberOfSeatsRequested;
    }

    /**
     * Sets the number of seats requested.
     * @param numberOfSeatsRequested The number of seats requested.
     */
    public void setNumberOfSeatsRequested(int numberOfSeatsRequested) {
        this.numberOfSeatsRequested = numberOfSeatsRequested;
    }

    /**
     * Returns the price band of the requested seats.
     * @return The price band of the requested seats.
     */
    public PriceBand getPriceBand() {
        return priceBand;
    }

    /**
     * Sets the price band of the requested seats.
     * @param priceBand The price band of the requeted seats.
     */
    public void setPriceBand(PriceBand priceBand) {
        this.priceBand = priceBand;
    }

    /**
     * Returns the booking fee for the reservation.
     * @return The booking fee for the reservation.
     */
    public BigDecimal getBookingFee() {
        return bookingFee;
    }

    /**
     * Sets the booking fee for the reservation.
     * @param bookingFee The booking fee for the reservation.
     */
    public void setBookingFee(BigDecimal bookingFee) {
        this.bookingFee = bookingFee;
    }

}
