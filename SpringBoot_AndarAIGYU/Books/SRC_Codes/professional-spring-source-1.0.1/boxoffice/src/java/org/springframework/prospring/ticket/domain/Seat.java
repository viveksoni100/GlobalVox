package org.springframework.prospring.ticket.domain;

/**
 * Represents a seat in the boxoffice system.
 */
public class Seat {

    // the id of the seat.
    private long id;

    // the name of the seat.
    private String name;

    // the seat on the left side of this seat.
    private Seat leftSide;

    // the seat on the right side of this seat.
    private Seat rightSide;

    /**
     * Empty constructor to support javabean spec.
     */
    public Seat() {
    }

    /**
     * Constructs a new seat with a given name
     * @param name The name of the seat.
     */
    public Seat(String name) {
        this.name = name;
    }

    /**
     * Returns the id of this seat.
     * @return The id of this seat.
     */
    public long getId() {
		return id;
	}

    /**
     * Sets the id of this seat.
     * @param id The id of this seat.
     */
    public void setId(long id) {
		this.id = id;
	}

    /**
     * Returns the name of this seat.
     * @return The name of this seat.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this seat.
     * @param name The name of this seat.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the left side seat.
     * @return The left side seat, or null if non exists.
     */
    public Seat getLeftSide() {
		return leftSide;
	}

    /**
     * Sets the left side seat.
     * @param leftSide The left side seat.
     */
    public void setLeftSide(Seat leftSide) {
		this.leftSide = leftSide;
	}


    /**
     * Returns the right side seat.
     * @return The right side seat.
     */
    public Seat getRightSide() {
		return rightSide;
	}

    /**
     * Sets the right side seat.
     * @param rightSide The right side seat.
     */
    public void setRightSide(Seat rightSide) {
		this.rightSide = rightSide;
	}

}
