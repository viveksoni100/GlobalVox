package org.springframework.prospring.ticket.web;

import java.util.*;
import java.math.*;

import javax.servlet.http.*;

import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.view.*;
import org.springframework.web.servlet.mvc.*;
import org.springframework.web.util.*;

/**
 * Form controller issuing a ReservationRequest on behalf of the user.
 * The user has selected a show, a price band and a date in the previous
 * screen(s) and this form controller will create the reservation
 * request based on this. Together with the amount of seats the user will
 * be selected, this form controller completes the request.<p>
 *  
 * Input:
 * <ul>
 *   <li><code>performanceId</code>: the performance identifier</li>
 *   <li><code>priceBandId</code>: the price band identifier</li>
 * </ul>
 * Error conditions:
 * <ul>
 *  <li>When not enough seats are available anymore</li>
 * </ul>
 */
public class BookSeatsController extends SimpleFormController {

    private final static int DEFAULT_MINUTES_TO_HOLD = 5;
    private final static int DEFAULT_DEFAULT_NUMBER_OF_SEATS_SELECTED = 1;
    private final static int DEFAULT_MAXIMUM_NUMBER_OF_SEATS = 12;
    private final static BigDecimal DEFAULT_BOOKING_FEE = new BigDecimal(0.0);

    // the box office service this controller uses.
    private BoxOffice boxOffice;

    // the events calendar this controller uses.
    private EventsCalendar eventsCalendar;

    // the number of seats that are selected by default.
    private int defaultNumberOfSeatsSelected = DEFAULT_DEFAULT_NUMBER_OF_SEATS_SELECTED;

    // the maximum number of seats that can be selected by the user.
    private int maximumNumberOfSeats = DEFAULT_MAXIMUM_NUMBER_OF_SEATS;

    // the number of minutes the seats are reserved for the user.
    private int minutesReservationWillBeValid = DEFAULT_MINUTES_TO_HOLD;

    // the booking fee for the reservation.
    // NOTE: Actually, this should not be in the presentation tier. It's a business logic
    // issue and should be one the backend (as part of the box office service). We keep
    // it here to be compatible with the code snippets in the book.
    private BigDecimal bookingFee = DEFAULT_BOOKING_FEE;

    /*
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
    protected Object formBackingObject(HttpServletRequest req) throws Exception {

        long performanceId = RequestUtils.getRequiredLongParameter(req, "performanceId");
        Performance performance = eventsCalendar.getPerformance(performanceId);

        long priceBandId = RequestUtils.getRequiredLongParameter(req, "priceBandId");
        PriceBand priceBand = boxOffice.getPriceBand(priceBandId);

        return createReservationRequest(performance, priceBand, minutesReservationWillBeValid);
    }

    /**
     * Binds the maximum number of seats the user will be able to select
     * (key = <code>maximumSeats</code>) and hte default selected value
     * (<code>defaultSelected</code>). 
     */
    protected Map referenceData(HttpServletRequest request, Object o, Errors errors) throws Exception {
        ReservationRequest reservationRequest = (ReservationRequest)o;
        int availableSeatsCount = boxOffice.getAvailableSeatsCount(
            reservationRequest.getPerformance(), reservationRequest.getPriceBand().getSeatClass());

        int maximumSeats = Math.min(maximumNumberOfSeats, availableSeatsCount);

        int defaultSelected = Math.min(defaultNumberOfSeatsSelected, availableSeatsCount);

        Map m = new HashMap();
        m.put("maximumSeats", new Integer(maximumSeats));
        m.put("defaultSelected", new Integer(defaultSelected));
        return m;
    }

    /**
     * @see SimpleFormController#onSubmit(
     *      javax.servlet.http.HttpServletRequest,
            javax.servlet.http.HttpServletResponse,
            Object,
            org.springframework.validation.BindException)
     */
    protected ModelAndView onSubmit(
        HttpServletRequest httpRequest,
        HttpServletResponse httpResponse,
        Object o,
        BindException errors) throws Exception {

        // the object is an already filled in reservation request
        ReservationRequest request = (ReservationRequest)o;
        Reservation reservation = null;
        try {
            reservation = boxOffice.allocateSeats(request);
        } catch (RequestedSeatNotAvailableException e) {
            errors.reject(e.getErrorCode(), "Requested seats are not available");
        } catch (NotEnoughSeatsException e) {
            errors.reject(e.getErrorCode(), "Could not allocate the number of seats requested");
        }

        // if errors occured showing the form view with the error messages.
        if (reservation == null) {
            return showForm(httpRequest, errors, getFormView());
        }

        // putting the reservation and performance in session, for future reference.
        WebUtils.setSessionAttribute(httpRequest, "reservation", reservation);
        WebUtils.setSessionAttribute(httpRequest, "reservationRequest", request);

        // redirecting the another controller that will show the reservation.
        // this is done to prevent the user from resubmitting the form.
        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    /**
     * Sets the box office service this controller should use.
     * @param boxOffice The box office service this controller should use.
     */
    public void setBoxOffice(BoxOffice boxOffice) {
        this.boxOffice = boxOffice;
    }

    /**
     * Sets the events calendar service this controller should use.
     * @param eventsCalendar The events calendar service this controller should use.
     */
    public void setEventsCalendar(EventsCalendar eventsCalendar) {
        this.eventsCalendar = eventsCalendar;
    }

    /**
     * Sets the number of minutes the seats are reserved for the user before they are purchased.
     * @param minutesReservationWillBeValid The number of minutes the seats are reserved for the user before they are purchased.
     */
    public void setMinutesReservationWillBeValid(int minutesReservationWillBeValid) {
        this.minutesReservationWillBeValid = minutesReservationWillBeValid;
    }

    /**
     * Sets the number of seats that are selected by default.
     * @param defaultNumberOfSeatsSelected The number of seats that are selected by default.
     */
    public void setDefaultNumberOfSeatsSelected(int defaultNumberOfSeatsSelected) {
        this.defaultNumberOfSeatsSelected = defaultNumberOfSeatsSelected;
    }

    /**
     * Sets the maximum number of seats a user can reserve for one reservation.
     * @param maximumNumberOfSeats The maximum number of seats a user can reserve for one reservation
     */
    public void setMaximumNumberOfSeats(int maximumNumberOfSeats) {
        this.maximumNumberOfSeats = maximumNumberOfSeats;
    }

    /**
     * Sets the booking fee for the reservation.
     * @param bookingFee The booking fee for the reservation.
     */
    public void setBookingFee(BigDecimal bookingFee) {
        this.bookingFee = bookingFee;
    }

    //====================================== Helper Method (some used for testing) ====================================

    /**
     * Creates a new ReservationRequest with the given values. This method can be used
     * in test cases to overriden and by that mock the created reservation request.
     * @param performance The performance of the request.
     * @param priceBand The price band of the request.
     * @param minutesToHold The number of minutes the requested seats should be reserved.
     * @return A new reservation request.
     */
    protected ReservationRequest createReservationRequest(
        Performance performance,
        PriceBand priceBand,
        int minutesToHold) {

        ReservationRequest request = new ReservationRequest();
        request.setPerformance(performance);
        request.setPriceBand(priceBand);
        request.holdFor(minutesToHold);
        request.setBookingFee(bookingFee);

        return request;
    }

}
