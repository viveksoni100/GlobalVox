package org.springframework.prospring.ticket.domain;

import java.util.*;

/**
 * Represets a price structure of a performance. A price structure holds the price bands
 * for a peformance, where each price band defines the price for a seat class.
 */
public class PriceStructure {

    // the id of this price structure.
    private long id;

    // the name of this price structure.
    private String name;

    // the price bands thie price structure holds.
    private Set priceBands;

    /**
     * Empty constructor to support javabean spec.
     */
    public PriceStructure() {
        priceBands = new HashSet();
    }

    /**
     * Constructs a new price structure with a given name.
     * @param name The name of the price structure.
     */
    public PriceStructure(String name) {
        priceBands = new HashSet();
        this.name = name;
    }

    /**
     * Returns the id of this prices structure.
     * @return The id of this prices structure.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this price structure.
     * @param id The id of this price structure.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the name of this price structure.
     * @return The name of this price structure.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this price structure.
     * @param name The name of this price structure.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the price bands of this price structure.
     * @return The price bands of this price structure.
     */
    public Set getPriceBands() {
        return priceBands;
    }

    /**
     * Sets the price bands of this price structure.
     * @param priceBands The price bands of this price structure.
     */
    public void setPriceBands(Set priceBands) {
        this.priceBands = priceBands;
    }

    /**
     * Adds the given price band to this price structure.
     * @param priceBand The price band to be added to this price structure.
     */
    public void addPriceBand(PriceBand priceBand) {
        priceBands.add(priceBand);
    }

    /**
     * Removes the given price band from this price structure.
     * @param priceBand The price band to be removed from this price structure.
     */
    public void removePriceBand(PriceBand priceBand) {
        priceBands.remove(priceBand);
    }

}
