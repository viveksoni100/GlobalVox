package org.springframework.prospring.ticket.domain.support;

import java.util.*;

import org.springframework.prospring.ticket.domain.*;

/**
 * A performance that holds information about available seats.
 */
public class PerformanceWithAvailability extends Performance {

    // the number of available seats for this performance.
    private int availableSeatCount;

    // a list of PriceBandWithAvailabilaty for this performance.
    private List priceBandWithAvailability;

	/**
     * Constructs a new performance with availability.
	 * @param id The id of this performance.
	 * @param dateAndTime The date of this performance.
	 * @param priceStructure The price structure of this performance.
	 * @param show The show of this performance.
	 */
	public PerformanceWithAvailability(
        long id,
        Date dateAndTime,
		PriceStructure priceStructure,
        Show show) {

        super(id, show, dateAndTime, priceStructure);
        this.availableSeatCount = 0;
        this.priceBandWithAvailability = new LinkedList();
	}

    public PerformanceWithAvailability(Performance performance) {
        super(performance.getId(), performance.getShow(), performance.getDateAndTime(), performance.getPriceStructure());
        availableSeatCount = 0;
        priceBandWithAvailability = new LinkedList();
    }

    /**
     * Returns the number of available seats in this performance.
     * @return The number of available seats in this performance.
     */
    public int getAvailableSeatCount() {
		return availableSeatCount;
	}

    /**
     * Returns a list of PriceBandWithAvailability for this performance.
     * @return A list of PriceBandWithAvailability for this performance.
     */
    public List getPriceBandWithAvailability() {
		return priceBandWithAvailability;
	}

    /**
     * Adds the given PriceBandWithAvailability to this peformance.
     * @param priceBandWithAvailability The PriceBandWithAvailability to be added to this performance.
     */
    public void add(PriceBandWithAvailability priceBandWithAvailability) {
		this.availableSeatCount += priceBandWithAvailability.getAvailableSeatCount();
		this.priceBandWithAvailability.add(priceBandWithAvailability);
	}
}
