package org.springframework.prospring.ticket.service;

import java.util.*;

import org.springframework.prospring.ticket.domain.*;

/**
 * Represents the event calendar of the system. The event calendar provides services regarding the
 * genres, shows, and performances available.
 */
public interface EventsCalendar {

    /**
     * Returns all genres defined in the system.
     * @return All genres defined in the system.
     */
    public List getAllGenres();

    /**
     * Returns all genres which currently have shows.
     * @return All genres which currently have shows.
     */
    public List getCurrentGenres();

    /**
     * Returns the performance associated with the given id.
     * @param performanceId The id of the requested performance.
     * @return The performance associated with the given id.
     */
    public Performance getPerformance(long performanceId);

    /**
     * Returns the show associated with the given id.
     * @param showId The id of the requested show.
     * @return The show associated with the given id.
     */
    public Show getShow(long showId);
}
