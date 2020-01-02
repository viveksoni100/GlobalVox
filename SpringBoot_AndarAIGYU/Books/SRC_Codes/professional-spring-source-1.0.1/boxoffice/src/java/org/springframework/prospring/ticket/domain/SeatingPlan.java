/*
 * Copyright (c) 2003-2005 JTeam B.V.
 * www.jteam.nl
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JTeam B.V. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with JTeam.
 */

package org.springframework.prospring.ticket.domain;

/**
 * Represents a seating plan for a show.
 */
public class SeatingPlan {

    // the id of this seating plan.
    private long id;

    // the name of this seating plan.
    private String name;

    /**
     * Empty constructor to support javabean sepc.
     */
    public SeatingPlan() {
    }

    /**
     * Constructs a new seating plan with a given name.
     * @param name The name of the seating plan.
     */
    public SeatingPlan(String name) {
        this.name = name;
    }

    /**
     * Returns the id of this seating plan.
     * @return The id of this seating plan.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this seating plan.
     * @param id The id of this seating plan.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the name of this seating plan.
     * @return The name of this seating plan.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this seating plan.
     * @param name The name of this seating plan.
     */
    public void setName(String name) {
        this.name = name;
    }

}
