package org.springframework.prospring.ticket.service;

import java.util.*;

import junit.framework.*;
import org.easymock.*;
import org.springframework.prospring.ticket.dao.*;
import org.springframework.prospring.ticket.domain.*;

/**
 * Tests the EventsCalendarImpl service.
 *
 * @see EventsCalendarImpl
 *
 * @author Uri Boness
 */
public class EventsCalendarImplTest extends TestCase {

    private EventsCalendarImpl eventsCalendar;

    private BoxOfficeDao daoMock;
    private MockControl daoMockControl;

    protected void setUp() throws Exception {
        daoMockControl = MockControl.createControl(BoxOfficeDao.class);
        daoMock = (BoxOfficeDao)daoMockControl.getMock();

        eventsCalendar = new EventsCalendarImpl();
        eventsCalendar.setBoxOfficeDao(daoMock);
    }

    /**
     * Tests the getCurrentGenres() method of the EventsCalendar service.
     * @see EventsCalendar#getCurrentGenres()
     * @throws Exception
     */
    public void testGetCurrentGenres() throws Exception {

        // setting the expectations
        List expectedResult = new ArrayList();
        daoMock.getCurrentGenres();
        daoMockControl.setReturnValue(expectedResult);

        daoMockControl.replay();

        // executing the tested method
        List result = eventsCalendar.getCurrentGenres();

        // assertions & verifications
        assertSame(expectedResult, result);
        daoMockControl.verify();
    }

    /**
     * Tests the getAllGenres() method of the EventsCalendar service.
     * @see EventsCalendar#getAllGenres()
     * @throws Exception
     */
    public void testGetAllGenres() throws Exception {

        // setting the expectations
        List expectedResult = new ArrayList();
        daoMock.getAllGenres();
        daoMockControl.setReturnValue(expectedResult);

        daoMockControl.replay();

        // executing the tested method
        List result = eventsCalendar.getAllGenres();

        // assertions & verifications
        assertSame(expectedResult, result);
        daoMockControl.verify();
    }

    /**
     * Tests the getPerformances(id) method of the EventsCalendar service.
     * @see EventsCalendar#getPerformance(long)
     * @throws Exception
     */
    public void testGetPerformance() throws Exception {

        // setting the expectations
        Performance expectedResult = new Performance(null, new Date(), null);
        daoMock.getPerformance(1);
        daoMockControl.setReturnValue(expectedResult);

        daoMockControl.replay();

        // executing the tested method
        Performance result = eventsCalendar.getPerformance(1);

        // assertions & verifications
        assertEquals(expectedResult, result);
        daoMockControl.verify();
    }

    /**
     * Tests the getShow(id) method of the EventsCalendar service.
     * @see EventsCalendar#getShow(long)
     * @throws Exception
     */
    public void testGetShow() throws Exception {

        // setting the expectations
        Show expectedResult = new Show("show");
        daoMock.getShow(1);
        daoMockControl.setReturnValue(expectedResult);

        daoMockControl.replay();

        // executing the tested method
        Show result = eventsCalendar.getShow(1);

        // assertions & verifications
        assertSame(expectedResult, result);
        daoMockControl.verify();
    }

}