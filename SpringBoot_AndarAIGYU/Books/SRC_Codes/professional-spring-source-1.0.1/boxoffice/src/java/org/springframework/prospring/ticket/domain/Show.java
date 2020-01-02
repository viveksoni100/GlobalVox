package org.springframework.prospring.ticket.domain;

import java.util.*;

/**
 * Represets a show in the boxoffice system.
 */
public class Show {

    // the if of this show.
    private long id;

    // the name of this show.
    private String name;

    // the performances of this show.
    private Set performances;

    // the seating plan for this show.
    private SeatingPlan seatingPlan;

    /**
     * Empty constructor to support javabean spec.
     */
    public Show() {
        performances = new HashSet();
    }

    /**
     * Constructs a new show with a given name.
     * @param name The name of the show.
     */
    public Show(String name) {
        performances = new HashSet();
        this.name = name;
    }

    /**
     * Returns the id of this show.
     * @return The id of this show.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this show.
     * @param id The id of this show.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the name of this show.
     * @return The name of this show.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this show.
     * @param name The name of this show.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the seating plan for this show.
     * @return The seating plan for this show.
     */
    public SeatingPlan getSeatingPlan() {
        return seatingPlan;
    }

    /**
     * Sets the seating plan for this show.
     * @param seatingPlan The seating plan for this show.
     */
    public void setSeatingPlan(SeatingPlan seatingPlan) {
        this.seatingPlan = seatingPlan;
    }

    /**
     * Returns a set of all performances of this show.
     * @return A set of all performances of this show.
     */
    public Set getPerformances() {
        return performances;
    }

    /**
     * Sets the performances of this show.
     * @param performances The performances of this show.
     */
    public void setPerformances(Set performances) {
        this.performances = performances;
    }

    /**
     * Adds the given performance to this show.
     * @param performance The peformance to be added to this show.
     */
    public void addPerformance(Performance performance) {
        performance.setShow(this);
        performances.add(performances);
    }

    /**
     * Removes the given peformance from this show.
     * @param performance The performance to be removed from this show.
     */
    public void removePerformance(Performance performance) {
        performances.remove(performance);
    }

}
