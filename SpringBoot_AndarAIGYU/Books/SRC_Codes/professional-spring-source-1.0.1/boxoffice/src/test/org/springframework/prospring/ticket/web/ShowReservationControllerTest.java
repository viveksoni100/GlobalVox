package org.springframework.prospring.ticket.web;

import java.math.*;

import junit.framework.*;
import org.springframework.mock.web.*;
import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.web.servlet.*;

/**
 * Tests the ShowReservationController.
 *
 * @see ShowReservationController
 *
 * @author Uri Boness
 */
public class ShowReservationControllerTest extends TestCase {

    /**
     * Tests the correctness of the model and view returned for the success view.
     * @throws Exception
     */
    public void testCorrectModelAndView() throws Exception {

        // creating the objects.

        Performance performance = new Performance();
        PriceBand priceBand = new PriceBand();
        ReservationRequest request = new ReservationRequest();
        request.setPerformance(performance);
        request.setPriceBand(priceBand);
        request.holdFor(5);
        request.setBookingFee(new BigDecimal(3.0));

        Booking booking = new Booking();
        Seat s1 = new Seat("s1");
        Seat s2 = new Seat("s2");
        Seat s3 = new Seat("s3");
        s1.setRightSide(s2);
        s2.setLeftSide(s1);
        s2.setRightSide(s3);
        s3.setLeftSide(s2);
        Seat[] seats = new Seat[] { s1, s2, s3 };
        Reservation reservation = new Reservation(seats, booking);

        MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/reserveSeats.html");
        httpRequest.getSession(true).setAttribute("reservationRequest", request);
        httpRequest.getSession(true).setAttribute("reservation", reservation);

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        // executing tested method.
        ShowReservationController showReservationController = new ShowReservationController() {
            protected boolean areAdjacentSeats(Seat[] seatList) {
                return true;
            }
        };
        showReservationController.setViewName("viewName");
        ModelAndView mav = showReservationController.handleRequest(httpRequest, httpResponse);

        // assertions & verifications.
        assertEquals("view name", "viewName", mav.getViewName());
        assertSame("reservation", reservation, mav.getModel().get("reservation"));
        assertSame("performance", performance, mav.getModel().get("performance"));
        assertEquals("minutesReservationWillBeValid", new Integer(5), mav.getModel().get("minutesReservationWillBeValid"));
        assertEquals("bookingFee", 3.0, ((BigDecimal)mav.getModel().get("bookingFee")).doubleValue(), 0.001);
        assertTrue("seat adjacent", ((Boolean)mav.getModel().get("seatsAdjacent")).booleanValue());
        assertEquals("seatsCount", new Integer(3), mav.getModel().get("seatsCount"));

        assertSame("reservation in session", reservation, httpRequest.getSession().getAttribute("reservation"));
        assertSame("reservation request in session", request, httpRequest.getSession().getAttribute("reservationRequest"));

    }


    /**
     * Tests the areAdjacentSeats(Seat[]) method of the controller.
     * @see ShowReservationController#areAdjacentSeats(org.springframework.prospring.ticket.domain.Seat[])
     * @throws Exception
     */
    public void testAreAdjacentSeats() throws Exception {

        Seat s1 = new Seat("s1");
        Seat s2 = new Seat("s2");
        Seat s3 = new Seat("s3");
        Seat s4 = new Seat("s4");
        Seat s5 = new Seat("s5");
        s1.setRightSide(s2);
        s2.setLeftSide(s1);
        s2.setRightSide(s3);
        s3.setLeftSide(s2);
        s4.setRightSide(s5);
        s5.setLeftSide(s4);

        Seat[] singleSeat = new Seat[] { s1 };
        Seat[] adjacent = new Seat[] { s1, s2, s3 };
        Seat[] notAdjacent = new Seat[] { s1, s2, s3, s4, s5};

        ShowReservationController showReservationController = new ShowReservationController();

        assertTrue(showReservationController.areAdjacentSeats(singleSeat));
        assertTrue(showReservationController.areAdjacentSeats(adjacent));
        assertFalse(showReservationController.areAdjacentSeats(notAdjacent));
    }

}