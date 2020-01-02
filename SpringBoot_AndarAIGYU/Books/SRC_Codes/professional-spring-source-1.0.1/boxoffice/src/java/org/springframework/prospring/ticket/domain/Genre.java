package org.springframework.prospring.ticket.domain;

import java.util.*;

/**
 * Represents a genere of shows.
 */
public class Genre {

    // the id of this genere
    private long id;

    // the name of this genre
    private String name;

    // the shows of this genere
    private Set shows;

    /**
     * Empty contructor for this genere (to support javabean spec.)
     */
    public Genre() {
        shows = new HashSet();
    }

    /**
     * Constructs a new genre with a given name.
     * @param name The name of this genre.
     */
    public Genre(String name) {
        shows = new HashSet();
        this.name = name;
    }

    /**
     * Returns the id of this genre.
     * @return The id of this genre.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this genre.
     * @param id The id of this genre.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the name of this genre.
     * @return The name of this genre.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this genre.
     * @param name The name of this genre.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the shows of this genre.
     * @return The shows of this genre.
     */
    public Set getShows() {
        return shows;
    }

    /**
     * Sets the shows of this genre.
     * @param shows The shows of this genre.
     */
    public void setShows(Set shows) {
        this.shows = shows;
    }

    /**
     * Adds the given show to this genre.
     * @param show The show to be added to this genre.
     */
    public void addShow(Show show) {
        shows.add(show);
    }

    /**
     * Removes the given show from this genre.
     * @param show The show to be removed from this genre.
     */
    public void removeShow(Show show) {
        shows.remove(show);
    }

}
