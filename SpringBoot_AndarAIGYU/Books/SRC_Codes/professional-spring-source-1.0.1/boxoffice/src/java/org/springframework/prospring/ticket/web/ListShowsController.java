package org.springframework.prospring.ticket.web;

import java.util.*;

import javax.servlet.http.*;

import org.springframework.prospring.ticket.service.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.*;

/**
 * Controller retrieving a list of genres from the *
 * Input: nothing<p>
 * Model:
 * <ul>
 *  <li><code>genres</code>: the list of genres</li>
 * </ul>
 */
public class ListShowsController extends AbstractController {

	// the events calendar service to use.
    private EventsCalendar eventsCalendar;

    // the name of the view this controller returns.
    private String viewName;

    /**
     * @see AbstractController#handleRequestInternal(
     *      javax.servlet.http.HttpServletRequest,
            javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Collection genres = eventsCalendar.getCurrentGenres();

        if (logger.isDebugEnabled()) {
            logger.debug("Genres: " + genres.size());
        }

    	return new ModelAndView(viewName, "genres", genres);
    }

    /**
     * Sets the events calendar this controller should use.
     * @param eventsCalendar The events calendar this controller should use.
     */
    public void setEventsCalendar(EventsCalendar eventsCalendar) {
        this.eventsCalendar = eventsCalendar;
    }

    /**
     * Sets the name of the view this controller returns.
     * @param viewName The name of the view this controller returns.
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

}
