package org.springframework.prospring.ticket.service;

import java.util.*;

import org.springframework.prospring.ticket.dao.*;
import org.springframework.prospring.ticket.domain.*;

/**
 * The default implementation of the EventsCalendar service.
 */
public class EventsCalendarImpl implements EventsCalendar {

    // the dao this service uses to retrieve the data from the database.
    private BoxOfficeDao boxOfficeDao;

    /**
     * Returns all genres which currently have shows.
     * @return All genres which currently have shows.
     */
    public List getCurrentGenres() {
        return boxOfficeDao.getCurrentGenres();
    }

    /**
     * Returns all genres defined in the system.     *
     * @return All genres defined in the system.
     */
    public List getAllGenres() {
        return boxOfficeDao.getAllGenres();
    }

    /**
     * Returns the performance associated with the given id.
     * @param performanceId The id of the requested performance.
     * @return The performance associated with the given id.
     */
    public Performance getPerformance(long performanceId) {
        return boxOfficeDao.getPerformance(performanceId);
    }

    /**
     * Returns the show associated with the given id.
     *
     * @param showId The id of the requested show.
     * @return The show associated with the given id.
     */
    public Show getShow(long showId) {
        return boxOfficeDao.getShow(showId);
    }

    /**
     * Sets the dao this service uses to retrive the data from the database..
     * @param boxOfficeDao The dao this service uses to retrive the data from the database.
     */
    public void setBoxOfficeDao(BoxOfficeDao boxOfficeDao) {
        this.boxOfficeDao = boxOfficeDao;
    }

}
