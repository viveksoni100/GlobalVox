package org.springframework.prospring.ticket.web;

import java.util.*;

import javax.servlet.http.*;
import org.springframework.web.servlet.mvc.*;
import org.springframework.web.servlet.*;
import org.springframework.web.util.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.prospring.ticket.domain.*;

/**
 * The controller that shows the reservation after the user chose the number of seats.
 * In general, it would be possible to have the BookSeatsController show this page, but then
 * when the user refreshes the page (using F5 for example) the form would be resubmitted and we would have
 * to handle this within the onSubmit() method of that controller. To save us this trouble, we redirect the
 * request to this controller. now when the user refreshes the page, the request will be handled by this controller
 * instead of the form controller.
 */
public class ShowReservationController extends AbstractController {

    private String viewName;

    /**
     * @see AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Reservation reservation = getReservationFromSession(request);
        ReservationRequest reservationRequest = getReservationRequestFromSession(request);


        ModelAndView mav = new ModelAndView(viewName);
        mav.addObject("reservation", reservation);
        mav.addObject("performance", reservationRequest.getPerformance());
        mav.addObject("minutesReservationWillBeValid", new Integer(reservationRequest.getMinutesToHold()));
        mav.addObject("bookingFee", reservationRequest.getBookingFee());
        mav.addObject("seatsAdjacent", ((areAdjacentSeats(reservation.getSeats())) ? Boolean.TRUE : Boolean.FALSE));
        mav.addObject("seatsCount", new Integer(reservation.getSeats().length));
        return mav;

    }

    /**
     * Sets the name of the view this controller returns.
     * @param viewName The name of the view this controller returns.
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    // retrieves the reservation from the session.
    protected Reservation getReservationFromSession(HttpServletRequest request) {
        return (Reservation)WebUtils.getRequiredSessionAttribute(request, "reservation");
    }

    // retrieves the reservation request from the session.
    protected ReservationRequest getReservationRequestFromSession(HttpServletRequest request) {
        return (ReservationRequest)WebUtils.getRequiredSessionAttribute(request, "reservationRequest");
    }

    /*
     * checks if the given seats are adjacent.
     *
     * algorithm: In general, if all seats are adjacent then there can only be
     *            on seat that its left seat is not in the list and only one seat
     *            that its right seat is not in the list. For the rest of the seats
     *            both left and right seats should be in the least.
     *
     *            we need to iterate over the seats and for each seat check if the
     *            left and right seats for that seat are in the leat. If we encounter
     *            more the one case where either the left or the right seat is not in
     *            the list then the seats are not adjacent.
     */
    protected boolean areAdjacentSeats(Seat[] seatList) {

        boolean foundLeftNotInList = false;
        boolean foundRightNotInList = false;

        // put all seats in a hashset for a faster access.
        HashSet seats = new HashSet();
        for (int i=0; i<seatList.length; i++) {
            seats.add(seatList[i]);
        }

        for (int i=0; i<seatList.length; i++) {
            Seat seat = seatList[i];
            if (!seats.contains(seat.getLeftSide())) {
                if (foundLeftNotInList) {
                    return false;
                }
                foundLeftNotInList = true;
            }
            if (!seats.contains(seat.getRightSide())) {
                if (foundRightNotInList) {
                    return false;
                }
                foundRightNotInList = true;
            }
        }

        return true;
    }

}
