package org.springframework.prospring.ticket.web;

import java.util.*;

import junit.framework.*;
import org.easymock.*;
import org.springframework.mock.web.*;
import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.domain.support.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.web.servlet.*;

/**
 * Tests the DisplayShowController.
 *
 * @see DisplayShowController
 *
 * @author Uri Boness
 */
public class DisplayShowControllerTest extends TestCase {

    // the controller under test.
    private DisplayShowController displayShowController;

    // the box office service mock.
    private BoxOffice boxOfficeMock;
    private MockControl boxOfficeMockControl;

    // the events calendar service mock.
    private EventsCalendar eventsCalendarMock;
    private MockControl eventsCalendarMockControl;

    /**
     * Setting up the test case.
     * Initializing the services mocks and the tested controller.
     * @throws Exception
     */
    protected void setUp() throws Exception {

        boxOfficeMockControl = MockControl.createControl(BoxOffice.class);
        boxOfficeMock = (BoxOffice)boxOfficeMockControl.getMock();

        eventsCalendarMockControl = MockControl.createControl(EventsCalendar.class);
        eventsCalendarMock = (EventsCalendar)eventsCalendarMockControl.getMock();

        displayShowController = new DisplayShowController();
        displayShowController.setBoxOffice(boxOfficeMock);
        displayShowController.setEventsCalendar(eventsCalendarMock);
        displayShowController.setViewName("displayShow");
        displayShowController.setSeatingPlansLocation("seatingPlansLocation");
        displayShowController.setShowInfosLocation("showInfosLocation");
    }

    /**
     * Tests that the controller returns the correct model and view.
     * @throws Exception
     */
    public void testCorrectModelAndView() throws Exception {

        // creating the objects
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/displayShow.html");
        request.addParameter("showId", "1");

        MockHttpServletResponse response = new MockHttpServletResponse();

        // setting expectations
        SeatingPlan seatingPlan = new SeatingPlan("seatingPlan");
        seatingPlan.setId(1);
        Show show = new Show("show");
        show.setId(1);
        show.setSeatingPlan(seatingPlan);
        eventsCalendarMock.getShow(1);
        eventsCalendarMockControl.setReturnValue(show);

        PerformanceWithAvailability performance = new PerformanceWithAvailability(1, new Date(), null, show);
        performance.add(new PriceBandWithAvailability(1, null, new SeatClass("code1", "desc1"), 2));
        performance.add(new PriceBandWithAvailability(1, null, new SeatClass("code2", "desc2"), 1));
        performance.add(new PriceBandWithAvailability(1, null, new SeatClass("code3", "desc3"), 3));
        PerformanceWithAvailability[] performances = new PerformanceWithAvailability[] {
            performance
        };

        boxOfficeMock.getAvailabilityForPerformances(show);
        boxOfficeMockControl.setReturnValue(performances);

        eventsCalendarMockControl.replay();
        boxOfficeMockControl.replay();

        // executing tested method
        ModelAndView mav = displayShowController.handleRequest(request, response);

        // assertions & verifications
        assertEquals("view name", "displayShow", mav.getViewName());
        assertEquals("seating plan url", "seatingPlansLocation/1.htm", mav.getModel().get("seatingPlanUrl"));
        assertEquals("show info url", "showInfosLocation/1.htm", mav.getModel().get("showInfoUrl"));
        assertSame("performances with availability", performances, mav.getModel().get("performances"));
        assertEquals("performances count", new Integer(performances.length), mav.getModel().get("performancesCount"));
        assertEquals("seat class count", 3, ((SeatClass[])mav.getModel().get("seatClasses")).length);
        assertNotNull("last performance date", mav.getModel().get("lastPerformanceDate"));

        boxOfficeMockControl.verify();
        eventsCalendarMockControl.verify();
    }
}