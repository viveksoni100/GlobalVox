package org.springframework.prospring.ticket.domain;

import java.util.*;

/**
 * Represents a peformance of a show in the boxoffice system.
 */
public class Performance {

    // the id of this performance.
    private long id;

    // the date of this peformance.
    private Date dateAndTime;

    // the price structure for this peformance.
    private PriceStructure priceStructure;

    // the show this peformance belongs to.
    private Show show;

    /**
     * Empty constructor to support javabean spec.
     */
    public Performance() {
	}

    /**
     * Constructs a new performance with given show, date and price structure.
     * @param show The show the performance belongs to.
     * @param dateAndTime The date of the performance.
     * @param priceStructure The price structure of the performance.
     */
    public Performance(Show show, Date dateAndTime, PriceStructure priceStructure) {
        this(0, show, dateAndTime, priceStructure);
    }

    /**
     * Constructs a new performance with given id, show, date and price structure.
     * @param show The show the performance belongs to.
     * @param dateAndTime The date of the performance.
     * @param priceStructure The price structure of the performance.
     */
    public Performance(long id, Show show, Date dateAndTime, PriceStructure priceStructure) {
        this.id = id;
        this.show = show;
        this.dateAndTime = dateAndTime;
        this.priceStructure = priceStructure;
    }
    /**
     * Returns the id of this performance.
     * @return The id of this performance.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this performance.
     * @param id The id of this performance.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the date of this peformance.
     * @return The date of this performance.
     */
    public Date getDateAndTime() {
        return dateAndTime;
    }

    /**
     * Sets the date of this performance.
     * @param dateAndTime The date of this performance.
     */
    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    /**
     * Returns the price structure for this performance.
     * @return The price structure for this performance.
     */
    public PriceStructure getPriceStructure() {
        return priceStructure;
    }

    /**
     * Sets the price structure for this performance.
     * @param priceStructure The price structure for this performance.
     */
    public void setPriceStructure(PriceStructure priceStructure) {
        this.priceStructure = priceStructure;
    }

    /**
     * Returns the show this performance belongs to.
     * @return The show this performance belongs to.
     */
    public Show getShow() {
        return show;
    }

    /**
     * Sets the show this performance belongs to.
     * @param show The show this performance belongs to.
     */
    public void setShow(Show show) {
        this.show = show;
    }

}
