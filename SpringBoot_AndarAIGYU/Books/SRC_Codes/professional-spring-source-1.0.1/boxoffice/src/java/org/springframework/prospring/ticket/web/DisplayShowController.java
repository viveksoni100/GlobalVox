package org.springframework.prospring.ticket.web;

import java.util.*;

import javax.servlet.http.*;

import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.domain.support.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.web.bind.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.*;

/**
 * Controller retrieving a show from the EventsCalendar
 * to show it to the user<p>
 * Input: showId<p>
 * Model:
 * <ul>
 *  <li><code>show</code>: the show to render
 *  <li><code>performances</code>: a list of PerformancesWithAvailability
 *  <li><code>showInfoUrl</code>: the url for the show information content.
 *  <li><code>seatingPlanUrl</code>: the url for hte seating plan content.
 *  <li><code>seatClasses</code>: a list of all seating classes
 * </ul>
 */
public class DisplayShowController extends AbstractController {

    private final static String STATIC_CONTENT_SUFFIX = ".htm";
    private final static String URL_PATH_SEPERATOR = "/";

    // the box office service this controller uses.
    private BoxOffice boxOffice;

    // the events calendar service this controller uses.
    private EventsCalendar eventsCalendar;

    // the name of the returned view.
    private String viewName;

    // the location of the shows information files
    private String showInfosLocation;

    // the location of the shows seating plan files.
    private String seatingPlansLocation;

    /**
     * @see AbstractController#handleRequestInternal(
     *      javax.servlet.http.HttpServletRequest,
            javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
        throws Exception {

        // if there's a reservation in the session then, canceling its booking and removing
        // it from the session.
        cancelBookingIfOneExists(httpRequest);

        long showId = RequestUtils.getRequiredLongParameter(httpRequest, "showId");

        Show show = eventsCalendar.getShow(showId);
        PerformanceWithAvailability[] availability = boxOffice.getAvailabilityForPerformances(show);

        Date lastPerformanceDate = (availability.length == 0) ? null : availability[availability.length-1].getDateAndTime();

        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("performances", availability);
        mav.addObject("performancesCount", new Integer(availability.length));
        mav.addObject("lastPerformanceDate", lastPerformanceDate);
        mav.addObject("show", show);
        mav.addObject("showInfoUrl", getInfoUrl(show));
        mav.addObject("seatingPlanUrl", getSeatingPlanUrl(show));
        mav.addObject("seatClasses", getSeatClasses(availability));

        return mav;
    }

    /**
     * Sets the box office service this controller should use.
     * @param boxOffice The box office service this controller should use.
     */
    public void setBoxOffice(BoxOffice boxOffice) {
        this.boxOffice = boxOffice;
    }

    /**
     * Sets the events calendar this controller should use.
     * @param eventsCalendar The events calendar this controller should use.
     */
    public void setEventsCalendar(EventsCalendar eventsCalendar) {
        this.eventsCalendar = eventsCalendar;
    }

    /**
     * Sets the location where show information files are located. The location is relative to
     * the web application root.
     * @param showInfosLocation The location where show information files are located.
     */
    public void setShowInfosLocation(String showInfosLocation) {
        this.showInfosLocation = showInfosLocation;
    }

    /**
     * Sets the location where the seating plan files are located. The location is relative to the
     * web application root.
     * @param seatingPlansLocation The location where the seating plan files are located.
     */
    public void setSeatingPlansLocation(String seatingPlansLocation) {
        this.seatingPlansLocation = seatingPlansLocation;
    }

    /**
     * Sets the name of the view this controller returns.
     * @param viewName The name of the view this controller returns.
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * Checks the session for a reservation. If one exists, removing it from the session and
     * canceling the related booking.
     * This is done to cancel the reservation made by users who didn't proceed to payment and
     * decided to choose another date (or price band) for a show.
     * @param request
     */
    protected void cancelBookingIfOneExists(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Reservation reservation = (Reservation)session.getAttribute("reservation");
        if (reservation == null) {
            return;
        }
        session.removeAttribute("reservation");
        session.removeAttribute("reservationRequest");
        Booking booking = reservation.getBooking();
        boxOffice.cancelBooking(booking);
    }

    // returns the url for the information file of the given show.
    private String getInfoUrl(Show show) {
        return showInfosLocation + URL_PATH_SEPERATOR + show.getId() + STATIC_CONTENT_SUFFIX;
    }

    // returns the url for the seating plan file for the given show.
    private String getSeatingPlanUrl(Show show) {
        return seatingPlansLocation + URL_PATH_SEPERATOR + show.getSeatingPlan().getId() + STATIC_CONTENT_SUFFIX;
    }

    // returns a list of all the available seat classes.
    private SeatClass[] getSeatClasses(PerformanceWithAvailability[] performances) {
        Set seatClasses = new HashSet();
        for (int i=0; i<performances.length; i++) {
            PerformanceWithAvailability performance = performances[i];
            List priceBands = performance.getPriceBandWithAvailability();
            for (Iterator iter = priceBands.iterator(); iter.hasNext();) {
                PriceBandWithAvailability priceBand = (PriceBandWithAvailability)iter.next();
                seatClasses.add(priceBand.getSeatClass());
            }
        }
        return (SeatClass[])seatClasses.toArray(new SeatClass[seatClasses.size()]);
    }
}
