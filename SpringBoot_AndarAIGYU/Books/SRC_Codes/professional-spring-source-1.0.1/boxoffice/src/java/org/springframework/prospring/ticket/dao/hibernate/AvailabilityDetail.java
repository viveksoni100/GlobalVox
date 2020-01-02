package org.springframework.prospring.ticket.dao.hibernate;

import org.springframework.prospring.ticket.domain.*;

/**
 * A helper class that associates the number of available seats with performance and price band.
 */
public class AvailabilityDetail {

    // the associated price band.
    private PriceBand priceBand;

    // the associated performance.
    private Performance performance;

    // the number of available seats.
    private int availableSeatCount;

    /**
     * Empty contructor to support javabeans spec.
     */
    public AvailabilityDetail() {
    }

    /**
     * Constructs a new AvailabilityDetails with given values.
	 * @param priceBand The associated price band.
	 * @param performance The associated performance.
	 * @param availableSeatCount The number of the available seats.
	 */
	public AvailabilityDetail(PriceBand priceBand, Performance performance, int availableSeatCount) {
		super();
		this.priceBand = priceBand;
		this.performance = performance;
		this.availableSeatCount = availableSeatCount;
	}

    /**
     * Returns the number of availabe seats for this association.
     * @return The number of availabe seats for this association.
     */
    public int getAvailableSeatCount() {
		return availableSeatCount;
	}

    /**
     * Sets the number of available seats for this association.
     * @param availableSeatCount The number of available seats for this association.
     */
    public void setAvailableSeatCount(int availableSeatCount) {
		this.availableSeatCount = availableSeatCount;
	}

    /**
     * Returns the performance of this association.
     * @return The performance of this association.
     */
    public Performance getPerformance() {
		return performance;
	}

    /**
     * Sets the performance of this association.
     * @param performance The performance of this association.
     */
    public void setPerformance(Performance performance) {
		this.performance = performance;
	}

    /**
     * Returns the price band of this association.
     * @return The price band of this association.
     */
    public PriceBand getPriceBand() {
		return priceBand;
	}

    /**
     * Sets the price band of this association.
     * @param priceBand The price band of this association.
     */
    public void setPriceBand(PriceBand priceBand) {
		this.priceBand = priceBand;
	}

}
