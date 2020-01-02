package org.springframework.prospring.ticket.web;

import java.math.*;

import javax.servlet.http.*;

import junit.framework.*;
import org.easymock.*;
import org.springframework.mock.web.*;
import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.view.*;
import org.springframework.validation.*;

/**
 * Tests the BookSeatsController.
 *
 * @see BookSeatsController
 *
 * @author Uri Boness
 */
public class BookSeatsControllerTest extends TestCase {

    // the box office service mock.
    private BoxOffice boxOfficeMock;
    private MockControl boxOfficeMockControl;

    // the events calendar service mock.
    private EventsCalendar eventsCalendarMock;
    private MockControl eventsCalendarMockControl;

    /**
     * Setting up the test - initializing the services mocks.
     * @throws Exception
     */
    protected void setUp() throws Exception {

        boxOfficeMockControl = MockControl.createControl(BoxOffice.class);
        boxOfficeMock = (BoxOffice)boxOfficeMockControl.getMock();

        eventsCalendarMockControl = MockControl.createControl(EventsCalendar.class);
        eventsCalendarMock = (EventsCalendar)eventsCalendarMockControl.getMock();
        
    }

    /**
     * Tests the correctness of the model and view returned for the form view.
     * @throws Exception
     */
    public void testCorrectModelAndViewForFormView() throws Exception {

        // creating the objects.
        MockHttpServletRequest httpRequest = new MockHttpServletRequest("GET", "/reserveSeats.html");
        httpRequest.addParameter("performanceId", "1");
        httpRequest.addParameter("priceBandId", "1");

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        // setting expectations.
        final Performance performance = new Performance();
        eventsCalendarMock.getPerformance(1);
        eventsCalendarMockControl.setReturnValue(performance);

        SeatClass seatClass = new SeatClass();
        final PriceBand priceBand = new PriceBand();
        priceBand.setSeatClass(seatClass);
        boxOfficeMock.getPriceBand(1);
        boxOfficeMockControl.setReturnValue(priceBand);

        boxOfficeMock.getAvailableSeatsCount(performance, seatClass);
        boxOfficeMockControl.setReturnValue(20);

        final ReservationRequest request = new ReservationRequest();
        request.setPerformance(performance);
        request.setPriceBand(priceBand);
        request.holdFor(5);

        boxOfficeMockControl.replay();
        eventsCalendarMockControl.replay();

        // executing tested method.
        BookSeatsController bookSeatsController = new BookSeatsController() {
            protected ReservationRequest createReservationRequest(Performance p, PriceBand pb, int minutesToHold) {
                assertSame(performance, p);
                assertSame(priceBand, pb);
                assertEquals(5, minutesToHold);
                return request;
            }
        };
        initController(bookSeatsController);

        ModelAndView mav = bookSeatsController.handleRequest(httpRequest, httpResponse);

        // assertions & verifications.
        assertEquals("the form view name", "formView", mav.getViewName());
        assertSame("the 'reservationRequest' command object", request, mav.getModel().get("reservationRequest"));
        assertEquals("the 'maximumSeats' allowed", new Integer(10), mav.getModel().get("maximumSeats"));
        assertEquals("The 'defaultSelected' number of seats", new Integer(2), mav.getModel().get("defaultSelected"));

        boxOfficeMockControl.verify();
        eventsCalendarMockControl.verify();
    }

    /**
     * Tests the correctness of the model and view returned for the success view.
     * @throws Exception
     */
    public void testCorrectModelAndViewForSuccessView() throws Exception {

        // creating the objects.

        Performance performance = new Performance();
        PriceBand priceBand = new PriceBand();
        final ReservationRequest request = new ReservationRequest();
        request.setPerformance(performance);
        request.setPriceBand(priceBand);
        request.holdFor(5);
        request.setBookingFee(new BigDecimal(3.0));

        MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/reserveSeats.html");
        httpRequest.addParameter("numberOfSeatsRequested", "3");
        httpRequest.getSession(true).setAttribute("formSessionAttributeName", request);

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        // setting expectations.
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
        boxOfficeMock.allocateSeats(request);
        boxOfficeMockControl.setReturnValue(reservation);

        boxOfficeMockControl.replay();

        // executing tested method.
        BookSeatsController bookSeatsController = new BookSeatsController() {
            protected String getFormSessionAttributeName(HttpServletRequest request) {
                return "formSessionAttributeName";
            }
            protected ReservationRequest createReservationRequest(Performance performance, PriceBand priceBand, int minutesToHold) {
                return request;
            }
        };
        initController(bookSeatsController);
        ModelAndView mav = bookSeatsController.handleRequest(httpRequest, httpResponse);

        // assertions & verifications.
        assertTrue(mav.getView() instanceof RedirectView);
        assertEquals("view name", "successView", ((RedirectView)mav.getView()).getUrl());
        assertSame("reservation in session", reservation, httpRequest.getSession().getAttribute("reservation"));
        assertSame("reservation request in session", request, httpRequest.getSession().getAttribute("reservationRequest"));

        boxOfficeMockControl.verify();

    }

    /**
     * Tests the correctness of the model and view returned for the success view.
     * @throws Exception
     */
    public void testCorrectModelAndViewOnSubmissionFailure() throws Exception {

        // creating the objects.

        Performance performance = new Performance();
        SeatClass seatClass = new SeatClass();
        PriceBand priceBand = new PriceBand();
        priceBand.setSeatClass(seatClass);
        ReservationRequest request = new ReservationRequest();
        request.setPerformance(performance);
        request.setPriceBand(priceBand);
        request.holdFor(5);
        request.setBookingFee(new BigDecimal(3.0));

        MockHttpServletRequest httpRequest = new MockHttpServletRequest("POST", "/reserveSeats.html");
        httpRequest.addParameter("numberOfSeatsRequested", "3");
        httpRequest.getSession(true).setAttribute("formSessionAttributeName", request);

        MockHttpServletResponse httpResponse = new MockHttpServletResponse();

        // setting expectations.
        boxOfficeMock.allocateSeats(request);
        boxOfficeMockControl.setThrowable(new NotEnoughSeatsException(1, 2, 3));

        boxOfficeMock.getAvailableSeatsCount(performance, seatClass);
        boxOfficeMockControl.setReturnValue(2);

        boxOfficeMockControl.replay();

        // executing tested method.
        BookSeatsController bookSeatsController = new BookSeatsController() {
            protected String getFormSessionAttributeName(HttpServletRequest request) {
                return "formSessionAttributeName";
            }
        };
        initController(bookSeatsController);
        ModelAndView mav = bookSeatsController.handleRequest(httpRequest, httpResponse);

        // assertions & verifications.
        assertEquals("the form view name", "formView", mav.getViewName());
        assertSame("the 'reservationRequest' command object", request, mav.getModel().get("reservationRequest"));
        assertEquals("the 'maximumSeats' allowed", new Integer(2), mav.getModel().get("maximumSeats"));
        assertEquals("The 'defaultSelected' number of seats", new Integer(2), mav.getModel().get("defaultSelected"));

        // verifying that the appropriate error is in the model.
        BindException bindException = (BindException)mav.getModel().get(BindException.ERROR_KEY_PREFIX + "reservationRequest");
        assertNotNull("bind exception not null", bindException);
        assertEquals("number of errors", 1, bindException.getErrorCount());
        assertEquals("error code", NotEnoughSeatsException.ERROR_CODE, bindException.getGlobalError().getCode());

        boxOfficeMockControl.verify();

    }

    // initializes the controller properties.
    private void initController(BookSeatsController bookSeatsController) {
        bookSeatsController.setSessionForm(true);
        bookSeatsController.setFormView("formView");
        bookSeatsController.setSuccessView("successView");
        bookSeatsController.setBoxOffice(boxOfficeMock);
        bookSeatsController.setEventsCalendar(eventsCalendarMock);
        bookSeatsController.setCommandName("reservationRequest");
        bookSeatsController.setCommandClass(ReservationRequest.class);
        bookSeatsController.setDefaultNumberOfSeatsSelected(2);
        bookSeatsController.setMaximumNumberOfSeats(10);
        bookSeatsController.setMinutesReservationWillBeValid(5);
    }

}