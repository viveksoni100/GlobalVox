package org.springframework.prospring.ticket.domain;

import org.springframework.util.*;

/**
 * Represents a seat class (type) in the boxoffice system.
 */
public class SeatClass {

    // the id of this seat class.
    private long id;

    // the code of this seat class.
    private String code;

    // the description of this seat class.
    private String description;

    /**
     * Empty constructor to support javabean spec.
     */
    public SeatClass() {
    }

    /**
     * Constructs a new seat type with given code and description.
     * @param code The code of the seat class.
     * @param description The description of the seat class.
     */
    public SeatClass(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Returns the id of this seat class.
     * @return The id of this seat class.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this seat class.
     * @param id The id of this seat class.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the code of this seat class.
     * @return The code of this seat class.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code of this seat class.
     * @param code The code of this seat class.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the description of this seat class.
     * @return The description of this seat class.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this seat class.
     * @param description The description of this seat class.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see Object#equals(Object)
     */
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof SeatClass)) {
            return false;
        }

        SeatClass seatClass = (SeatClass) o;

        return ObjectUtils.nullSafeEquals(code, seatClass.code) &&
            ObjectUtils.nullSafeEquals(description, seatClass.description);
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = (code != null) ? code.hashCode() : 0;
        result = 31 * result + ((description != null) ? description.hashCode() : 0);
        return result;
    }
}
