
package org.springframework.prospring.ticket.web;

import java.util.*;

import junit.framework.*;
import org.easymock.*;
import org.springframework.mock.web.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.web.servlet.*;

/**
 * Tests the ListShowsController.
 *
 * @see ListShowsController
 *
 * @author Uri Boness
 */
public class ListShowsControllerTest extends TestCase {

    // the controller under test.
    private ListShowsController listShowsController;

    // the events calendar service mock.
    private EventsCalendar eventsCalendarMock;
    private MockControl eventsCalendarMockControl;

    /**
     * Setting up the test - initializing the controller and the services mocks.
     * @throws Exception
     */
    protected void setUp() throws Exception {

        eventsCalendarMockControl = MockControl.createControl(EventsCalendar.class);
        eventsCalendarMock = (EventsCalendar)eventsCalendarMockControl.getMock();

        listShowsController = new ListShowsController();
        listShowsController.setEventsCalendar(eventsCalendarMock);
        listShowsController.setViewName("viewName");
    }

    /**
     * Tests the the controller returns the correct model.
     * @throws Exception
     */
    public void testCorrectModelAndView() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/listShows.html");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // setting the expectations
        List currentGenres = new ArrayList();
        eventsCalendarMock.getCurrentGenres();
        eventsCalendarMockControl.setReturnValue(currentGenres);

        eventsCalendarMockControl.replay();

        // executing the tested method.
        ModelAndView mav = listShowsController.handleRequest(request, response);

        // assertions & verifications
        assertEquals("view name", "viewName", mav.getViewName());
        assertSame("model contains genres", currentGenres, mav.getModel().get("genres"));
        eventsCalendarMockControl.verify();

    }
}