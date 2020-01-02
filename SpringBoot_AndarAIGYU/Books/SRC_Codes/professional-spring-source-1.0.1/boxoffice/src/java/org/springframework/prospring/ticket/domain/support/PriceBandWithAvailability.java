package org.springframework.prospring.ticket.domain.support;

import java.math.*;

import org.springframework.prospring.ticket.domain.*;

/**
 * A price band that holds information about available seats (seats that belong to this price band).
 */
public class PriceBandWithAvailability extends PriceBand {

    // the number of available seat in this price band.
    private int availableSeatCount;

	/**
     * Constructs a new PriceBandWithAvailability.
	 * @param id The id of this price band.
	 * @param price The price of this price band.
	 * @param seatClass The seat class that is associated with this price band.
     * @param availableSeatCount The number of available seats in this price band.
	 */
	public PriceBandWithAvailability(
        long id,
        BigDecimal price,
        SeatClass seatClass,
        int availableSeatCount) {

        super(id, seatClass, price);
        this.availableSeatCount = availableSeatCount;
	}

    /**
     * Constructs a new PriceBandWithAvailability.
     * @param priceBand The price band.
     * @param availableSeatCount The number of seat available for the price band.
     */
    public PriceBandWithAvailability(PriceBand priceBand, int availableSeatCount) {
        this(priceBand.getId(), priceBand.getPrice(), priceBand.getSeatClass(), availableSeatCount);
    }

    /**
     * Returns the number of available seats in this price band.
     * @return The number of available seats in this price band.
     */
    public int getAvailableSeatCount() {
		return availableSeatCount;
	}
}
