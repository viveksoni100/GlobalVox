package org.springframework.prospring.ticket.domain;

import java.math.*;

/**
 * Represets a price band in the boxoffice system. A price band associates a price
 * with a seat class.
 */
public class PriceBand {

    // the id of this price band.
    private long id;

    // the price for the associated seat class.
    private BigDecimal price;

    // the seat class associated with this price band.
    private SeatClass seatClass;

    /**
     * Empty constructor to support javabean spec.
     */
    public PriceBand() {
	}

    /**
     * Constructs a new price band with given associated seat class and price.
     * @param seatClass The associated seat class.
     * @param price The price of the associated seat class.
     */
    public PriceBand(SeatClass seatClass, BigDecimal price) {
		this(0, seatClass, price);
    }

    /**
     * Constructs a new price band with given associated seat class, id, and price.
     * @param id The id of this price band.
     * @param seatClass The associated seat class.
     * @param price The price of the associated seat class.
     */
    public PriceBand(long id, SeatClass seatClass, BigDecimal price) {
        this.id = id;
        this.seatClass = seatClass;
        this.price = price;
    }

    /**
     * Returns the id of this price band.
     * @return The id of this price band.
     */
    public long getId() {
		return id;
	}

    /**
     * Sets the id of this price band.
     * @param id The id of this price band.
     */
    public void setId(long id) {
		this.id = id;
	}

    /**
     * Returns the price of this price band.
     * @return The price of this price band.
     */
    public BigDecimal getPrice() {
		return price;
	}

    /**
     * Sets the price of this price band.
     * @param price The price of this price band.
     */
    public void setPrice(BigDecimal price) {
		this.price = price;
	}

    /**
     * Returns the seat class that is associated with this price band.
     * @return The seat class that is associated with this price band.
     */
    public SeatClass getSeatClass() {
		return seatClass;
	}

    /**
     * Sets the seat class that is associated with this price band.
     * @param seatClass The seat class that is associated with this price band.
     */
    public void setSeatClass(SeatClass seatClass) {
		this.seatClass = seatClass;
	}
}
