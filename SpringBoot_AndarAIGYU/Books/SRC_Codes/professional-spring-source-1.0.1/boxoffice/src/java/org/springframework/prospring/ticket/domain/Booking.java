package org.springframework.prospring.ticket.domain;

import java.math.*;
import java.util.*;

/**
 * Represents a booking in the boxoffice system.
 */
public class Booking {

    // the id of this booking.
    private long id;

    // the date on which this booking was made.
    private Date dateMade;

    // the date until which this booking will be reserved.
    private Date reservedUntil;

    // the price of this booking.
    private BigDecimal price;

    // the purchase of this booking
    private Purchase purchase;

    // the number of seats reserved for this booking.
    // in realwork we would probably keep a list of all reserved seats.
    // this will be used when checking if the seats for a booking are
    // still available.
    private int seatCount;

    /**
     * Empty constructor to support javabean spec.
     */
    public Booking() {
    }

    /**
     * Constructs a new booking.
     * @param reserveUntil The date till which this booking is reserved.
     * @param price The price of this booking.
     */
    public Booking(int seatCount, Date reserveUntil, BigDecimal price) {
        this(seatCount, new Date(), reserveUntil, price);
    }

    /**
     * Constructs a new booking.
     * @param dateMade The date this booking was made.
     * @param reservedUntil The date till which this booking is reserved
     * @param price The price of this booking.
     */
    public Booking(int seatCount, Date dateMade, Date reservedUntil, BigDecimal price) {
        this.seatCount = seatCount;
        this.dateMade = dateMade;
        this.reservedUntil = reservedUntil;
        this.price = price;
    }

    /**
     * Returns the id of this booking.
     * @return The id of this booking.
     */
    public long getId() {
		return id;
	}

    /**
     * Set the id of this booking.
     * @param id The id of this booking.
     */
    public void setId(long id) {
		this.id = id;
	}

    /**
     * Returns the date on which this booking was made.
     * @return The date on which this booking was made.
     */
    public Date getDateMade() {
		return dateMade;
	}

    /**
     * Sets the date on which this booking was made.
     * @param dateMade The date on which this booking was made.
     */
    public void setDateMade(Date dateMade) {
		this.dateMade = dateMade;
	}

    /**
     * Returns the price of this booking.
     * @return The price of this booking.
     */
    public BigDecimal getPrice() {
		return price;
	}

    /**
     * Sets the price of this booking.
     * @param price The price of this booking.
     */
    public void setPrice(BigDecimal price) {
		this.price = price;
	}

    /**
     * Returns the purchase of this booking if one exists.
     * @return The purchase of this booking or null if non exists.
     */
    public Purchase getPurchase() {
		return purchase;
	}

    /**
     * Sets the purchase of this booking.
     * @param purchase The purchase of this booking.
     */
    public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

    /**
     * Returns the date untill which this booking will be reserved.
     * @return The date untill which this booking will be reserved.
     */
    public Date getReservedUntil() {
		return reservedUntil;
	}

    /**
     * Sets the date until which this booking will be reserved.
     * @param reservedUntil The date until which this booking will be reserved.
     */
    public void setReservedUntil(Date reservedUntil) {
		this.reservedUntil = reservedUntil;
	}

    /**
     * Returns the number of seats reserved by this booking.
     * @return The number of seats reserved by this booking.
     */
    public int getSeatCount() {
        return seatCount;
    }

    /**
     * Sets the number of seats reserved by this booking.
     * @param seatCount The number of seats reserved by this booking.
     */
    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

}
