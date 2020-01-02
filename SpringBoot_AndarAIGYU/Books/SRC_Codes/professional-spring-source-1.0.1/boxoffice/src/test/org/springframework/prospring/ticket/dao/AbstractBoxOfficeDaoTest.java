/*
 * Copyright (c) 2003-2005 JTeam B.V.
 * www.jteam.nl
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JTeam B.V. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with JTeam.
 */

package org.springframework.prospring.ticket.dao;

import java.beans.*;
import java.math.*;
import java.util.*;

import org.springframework.beans.*;
import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.domain.support.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.test.*;

/**
 * A base class for all BoxOfficeDao tests.
 *
 * @author Uri Boness
 */
public abstract class AbstractBoxOfficeDaoTest extends AbstractTransactionalDataSourceSpringContextTests {

    // the dao to be tested.
    protected BoxOfficeDao dao;

    // helper class used to manipulate the data in the database.
    private BoxOfficeDBHelper dbHelper;

    /**
     * Setting up the current test
     * @throws Exception
     */
    protected void onSetUpInTransaction() throws Exception {
        dao = createDao();
        dbHelper = new BoxOfficeDBHelper(jdbcTemplate);
        dbHelper.clearDatabase();
    }

    /**
     * Returns the dao to be tested.
     * @return The dao to be tested.
     */
    protected abstract BoxOfficeDao createDao();

    /**
     * Called sometimes just before the assertions in the test. This callback method can be used for example
     * to flush memory state to the database (in Hibernate or JDO for example).
     */
    protected abstract void beforeAssertions();

    /**
     * Tests the reserveSeats(...) method when all requested seats are available.
     * @see BoxOfficeDao#reserveSeats(
     *      org.springframework.prospring.ticket.domain.Seat[],
     *      org.springframework.prospring.ticket.domain.Performance,
     *      org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testReserveSeatsWithAvailableSeats() throws Exception {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        calendar.add(Calendar.MINUTE, 5);
        Date fiveMinutesLater = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date aWeekLater = calendar.getTime();

        // populating the database
        dbHelper.insertShow(1, "show1", -1, -1);

        dbHelper.insertPerformance(1, aWeekLater, -1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), -1, -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, -1, 1);
        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the cooresponding domain model
        Show show = new Show("show1");
        show.setId(1);

        Performance performance = new Performance(1, show, aWeekLater, null);

        Seat seat1 = new Seat("seat1");
        seat1.setId(1);
        Seat seat2 = new Seat("seat2");
        seat2.setId(2);
        Seat seat3 = new Seat("seat3");
        seat3.setId(3);
        seat1.setRightSide(seat2);
        seat2.setLeftSide(seat1);
        seat2.setRightSide(seat3);
        seat3.setLeftSide(seat2);
        Seat[] seats = new Seat[] {seat1, seat2, seat3};

        Booking booking = new Booking(3, date, fiveMinutesLater, new BigDecimal(3.0));
        booking.setId(1);

        // executing the tested method
        dao.reserveSeats(seats, performance, booking);

        beforeAssertions();

        // assertions
        long id = dbHelper.getBookingId(date, fiveMinutesLater, new BigDecimal(3.0));
        assertTrue(id != 0);
        assertTrue(dbHelper.verifyBookingOfSeat(1, id));
        assertTrue(dbHelper.verifyBookingOfSeat(2, id));
        assertTrue(dbHelper.verifyBookingOfSeat(3, id));
    }

    /**
     * Tests the reserveSeats(...) method when one of the seats is already booked.
     * @see BoxOfficeDao#reserveSeats(
     *      org.springframework.prospring.ticket.domain.Seat[],
     *      org.springframework.prospring.ticket.domain.Performance,
     *      org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testReserveSeatsWithSeatsWithValidBooking() throws Exception {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        calendar.add(Calendar.MINUTE, 5);
        Date fiveMinutesLater = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date aWeekLater = calendar.getTime();

        // populating the database
        dbHelper.insertShow(1, "show1", -1, -1);

        dbHelper.insertPerformance(1, aWeekLater, -1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), -1, -1);

        dbHelper.insertBooking(1, 1, date, fiveMinutesLater, new BigDecimal(3.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, 1, 1);
        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the cooresponding domain model
        Show show = new Show("show1");
        show.setId(1);

        Performance performance = new Performance(1, show, aWeekLater, null);

        Seat seat1 = new Seat("seat1");
        seat1.setId(1);
        Seat seat2 = new Seat("seat2");
        seat2.setId(2);
        Seat seat3 = new Seat("seat3");
        seat3.setId(3);
        seat1.setRightSide(seat2);
        seat2.setLeftSide(seat1);
        seat2.setRightSide(seat3);
        seat3.setLeftSide(seat2);
        Seat[] seats = new Seat[] {seat1, seat2, seat3};

        Booking booking = new Booking(3, date, fiveMinutesLater, new BigDecimal(3.0));
        booking.setId(2);

        // executing the tested method
        try {
            dao.reserveSeats(seats, performance, booking);
            fail("Exception RequestedSeatNotAvailableException was expected to be thrown");
        } catch (RequestedSeatNotAvailableException rsnae) {
            // expected behavior
        }
    }

    /**
     * Tests the reserveSeats(...) method when one of the seats is already booked, but its booking has expired.
     * @see BoxOfficeDao#reserveSeats(
     *      org.springframework.prospring.ticket.domain.Seat[],
     *      org.springframework.prospring.ticket.domain.Performance,
     *      org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testReserveSeatsWithSeatsWithExpiredBooking() throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -5);
        Date fiveMinuteEarlier = calendar.getTime();
        Date date = calendar.getTime();
        calendar.add(Calendar.MINUTE, 10);
        Date fiveMinutesLater = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date aWeekLater = calendar.getTime();

        // populating the database
        dbHelper.insertShow(1, "show1", -1, -1);

        dbHelper.insertPerformance(1, aWeekLater, -1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), -1, -1);

        dbHelper.insertBooking(1, 1, date, fiveMinuteEarlier, new BigDecimal(3.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, 1, 1);
        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the cooresponding domain model
        Show show = new Show("show1");
        show.setId(1);

        Performance performance = new Performance(1, show, aWeekLater, null);

        Seat seat1 = new Seat("seat1");
        seat1.setId(1);
        Seat seat2 = new Seat("seat2");
        seat2.setId(2);
        Seat seat3 = new Seat("seat3");
        seat3.setId(3);
        seat1.setRightSide(seat2);
        seat2.setLeftSide(seat1);
        seat2.setRightSide(seat3);
        seat3.setLeftSide(seat2);
        Seat[] seats = new Seat[] {seat1, seat2, seat3};

        Booking booking = new Booking(3, date, fiveMinutesLater, new BigDecimal(3.0));
        booking.setId(2);

        // executing the tested method
        dao.reserveSeats(seats, performance, booking);

        beforeAssertions();

        // assertions
        long id = dbHelper.getBookingId(date, fiveMinutesLater, new BigDecimal(3.0));
        assertTrue(id != 0);
        assertTrue(dbHelper.verifyBookingOfSeat(1, id));
        assertTrue(dbHelper.verifyBookingOfSeat(2, id));
        assertTrue(dbHelper.verifyBookingOfSeat(3, id));
    }

    /**
     * Tests the savePurchaseForBooking(Booking) method of BoxOfficeDao.
     * @see BoxOfficeDao#savePurchaseForBooking(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testSavePurchaseForBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MINUTE, 5);
        Date fiveMinutesFromNow = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date aWeekLater = cal.getTime();

        // populating the databaes
        dbHelper.insertShow(1, "show1", -1, -1);

        dbHelper.insertPerformance(1, aWeekLater, -1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), -1, -1);

        dbHelper.insertBooking(1, 3, now, fiveMinutesFromNow, new BigDecimal(5.0), -1);


        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, 1, 1);
        dbHelper.insertSeatStatus(1, 2, 1, 1);
        dbHelper.insertSeatStatus(1, 3, 1, 1);

        // creating the domain model
        Address address = new Address("country", "city", "street", "1234");
        Purchase purchase = new Purchase("abc", now, "john", "xxx", "john@mail.com", address, address);

        Booking booking = new Booking(3, now, fiveMinutesFromNow, new BigDecimal(5.0));
        booking.setId(1);
        booking.setPurchase(purchase);

        // executing the tested method
        dao.savePurchaseForBooking(booking);

        beforeAssertions();

        // assertions
        long purchaseId = dbHelper.getPurchaseId("abc", now, "john", "xxx", "john@mail.com", address, address);
        assertTrue(purchaseId != -1);
    }

    /**
     * Tests the checkAvailabilityForBooking(Booking) method of BoxOfficeDao.
     * @see BoxOfficeDao#checkAvailabilityForBooking(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testCheckAvailabilityForBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MINUTE, -10);
        Date tenMinutesEarlier = cal.getTime();
        cal.add(Calendar.MINUTE, 5);
        Date fiveMinutesEarlier = cal.getTime();
        cal.add(Calendar.MINUTE, 10);
        Date fiveMinutesFromNow = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date aWeekLater = cal.getTime();

        // populating the databaes
        dbHelper.insertShow(1, "show1", -1, -1);

        dbHelper.insertPerformance(1, aWeekLater, -1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), -1, -1);

        dbHelper.insertBooking(1, 3, tenMinutesEarlier, fiveMinutesEarlier, new BigDecimal(5.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, 1, 1);
        dbHelper.insertSeatStatus(1, 2, 1, 1);
        dbHelper.insertSeatStatus(1, 3, 1, 1);

        // creating the domain model
        Address address = new Address("country", "city", "street", "1234");
        Purchase purchase = new Purchase("abc", now, "john", "xxx", "john@mail.com", address, address);

        Booking booking = new Booking(3, now, fiveMinutesFromNow, new BigDecimal(5.0));
        booking.setId(1);
        booking.setPurchase(purchase);

        // executing the tested method
        boolean result = dao.checkAvailabilityForBooking(booking);

        beforeAssertions();

        // assertions
        assertTrue("seat availability", result);
    }

    /**
     * Tests the checkAvailabilityForBooking(Booking) method of BoxOfficeDao. In this case some of the reserved seats
     * are no longer available.
     * @see BoxOfficeDao#checkAvailabilityForBooking(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testCheckAvailabilityForBookingWithUnavailableSeats() throws Exception {

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MINUTE, -10);
        Date tenMinutesEarlier = cal.getTime();
        cal.add(Calendar.MINUTE, 5);
        Date fiveMinutesEarlier = cal.getTime();
        cal.add(Calendar.MINUTE, 10);
        Date fiveMinutesFromNow = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date aWeekLater = cal.getTime();

        // populating the databaes
        dbHelper.insertShow(1, "show1", -1, -1);

        dbHelper.insertPerformance(1, aWeekLater, -1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), -1, -1);

        dbHelper.insertBooking(1, 3, tenMinutesEarlier, fiveMinutesEarlier, new BigDecimal(5.0), -1);
        dbHelper.insertBooking(2, 1, now, fiveMinutesFromNow, new BigDecimal(4.0), -1);


        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, 1, 1);
        dbHelper.insertSeatStatus(1, 2, 1, 1);
        dbHelper.insertSeatStatus(1, 3, 2, 1);

        // creating the domain model
        Address address = new Address("country", "city", "street", "1234");
        Purchase purchase = new Purchase("abc", now, "john", "xxx", "john@mail.com", address, address);

        Booking booking = new Booking(3, now, fiveMinutesFromNow, new BigDecimal(5.0));
        booking.setId(1);
        booking.setPurchase(purchase);

        // executing the tested method
        boolean result = dao.checkAvailabilityForBooking(booking);

        beforeAssertions();

        // assertions
        assertFalse("seat availability", result);
    }

    /**
     * Tests the getAllGenres() method.
     * @see BoxOfficeDao#getAllGenres()
     * @throws Exception
     */
    public void testGetAllGenres() throws Exception {

        // populating the databaes with genres and shows
        dbHelper.insertGenre(1, "Opera");
        dbHelper.insertGenre(2, "Ballet");
        dbHelper.insertGenre(3, "Theatre");

        dbHelper.insertShow(1, "show1", 1, -1);
        dbHelper.insertShow(2, "show2", 1, -1);
        dbHelper.insertShow(3, "show3", 3, -1);

        // executing tested method
        List genres = dao.getAllGenres();

        // sorting result for expected order.
        sortByProperty(genres, "id");

        // assertions
        assertEquals("all genres returned", 3, genres.size());
        Genre genre1 = (Genre)genres.get(0);
        assertEquals("Opera", genre1.getName());
        Genre genre2 = (Genre)genres.get(1);
        assertEquals("Ballet", genre2.getName());
        Genre genre3 = (Genre)genres.get(2);
        assertEquals("Theatre", genre3.getName());

        Set shows1 = genre1.getShows();
        assertEquals("shows for genre " + genre1.getName(), 2, shows1.size());
        Set shows2 = genre2.getShows();
        assertEquals("shows for genre " + genre2.getName(), 0, shows2.size());
        Set shows3 = genre3.getShows();
        assertEquals("shows for genre " + genre3.getName(), 1, shows3.size());

    }

    /**
     * Tests the getCurrentGenres() method.
     * @see BoxOfficeDao#getCurrentGenres()
     * @throws Exception
     */
    public void testGetCurrentGenres() throws Exception {

        // populating the databaes with genres and shows
        dbHelper.insertGenre(1, "Opera");
        dbHelper.insertGenre(2, "Ballet");
        dbHelper.insertGenre(3, "Theatre");

        dbHelper.insertShow(1, "show1", 1, -1);
        dbHelper.insertShow(2, "show2", 1, -1);
        dbHelper.insertShow(3, "show3", 3, -1);

        // executing tested method
        List currentGenres = dao.getCurrentGenres();

        // sorting result for expected order.
        sortByProperty(currentGenres, "id");

        // assertions
        assertEquals("Current genres returned", 2, currentGenres.size());
        Genre genre1 = (Genre)currentGenres.get(0);
        Genre genre2 = (Genre)currentGenres.get(1);
        assertEquals("Opera", genre1.getName());
        assertEquals("Theatre", genre2.getName());

        Set shows1 = genre1.getShows();
        assertEquals("shows for genre " + genre1.getName(), 2, shows1.size());
        Set shows2 = genre2.getShows();
        assertEquals("shows for genre " + genre2.getName(), 1, shows2.size());
    }

    /**
     * Tests the getShow(id) method.
     * @see BoxOfficeDao#getShow(long)
     * @throws Exception
     */
    public void testGetShow() throws Exception {

        // inserting a show with performance to the database.
        dbHelper.insertGenre(1, "genre");
        dbHelper.insertSeatingPlan(1, "plan");
        dbHelper.insertShow(1, "show1", 1, 1);
        dbHelper.insertPerformance(1, new Date(), -1, 1);

        // executing the tested method.
        Show show = dao.getShow(1);

        // assertions
        assertNotNull(show);
        assertEquals("the show name", "show1", show.getName());

        Set performances = show.getPerformances();
        assertEquals("performances count of show " + show.getName(), 1, performances.size());
        assertEquals("peformance of show id", 1, ((Performance)performances.iterator().next()).getId());
        assertNotNull("seating plan of show " + show.getName(), show.getSeatingPlan());
        assertEquals("seating plan name of show " + show.getName(), "plan", show.getSeatingPlan().getName());
    }

    /**
     * Tests the getPerformance(id) method.
     * @see BoxOfficeDao#getPerformance(long)
     * @throws Exception
     */
    public void testGetPerformance() throws Exception {

        // inserting a show with performance to the database.
        dbHelper.insertShow(1, "show1", -1, -1);
        dbHelper.insertPerformance(1, new Date(), -1, 1);
        
        // executing the tested method.
        Performance performance = dao.getPerformance(1);

        // assertions
        assertNotNull(performance);

        Show show = performance.getShow();
        assertNotNull(show);
        assertEquals("the performance show name", "show1", show.getName());

    }

    /**
     * Tests the getPriceBand(id) method.
     * @see BoxOfficeDao#getPriceBand(long)
     * @throws Exception
     */
    public void testGetPriceBand() throws Exception {

        // inserting a price band with a seat class and price structure to the database.
        dbHelper.insertSeatClass(1, "code1", "desc1");
        dbHelper.insertPriceBand(1, new BigDecimal(5.0), -1, 1);

        // executing the tested method
        PriceBand priceBand = dao.getPriceBand(1);

        // assertions
        assertNotNull(priceBand);
        assertEquals("the price band price", new BigDecimal(5.0).doubleValue(), priceBand.getPrice().doubleValue(), 0.001);
        assertNotNull("the price band seat class", priceBand.getSeatClass());
        assertEquals("the price band seat class code", "code1", priceBand.getSeatClass().getCode());
        assertEquals("the price band seat class description", "desc1", priceBand.getSeatClass().getDescription());
    }

    /**
     * Tests the getPriceStructure(id) method.
     * @see BoxOfficeDao#getPriceStructure(long)
     * @throws Exception
     */
    public void testGetPriceStructure() throws Exception {

        // inserting a price structure with price band to the database.
        dbHelper.insertPriceStructure(1, "name");
        dbHelper.insertPriceBand(1, new BigDecimal(5.0), 1, -1);

        // executing the tested method
        PriceStructure priceStructure = dao.getPriceStructure(1);

        // assertions.
        assertNotNull("the price structure", priceStructure);
        assertEquals("the price structure name", "name", priceStructure.getName());
        assertEquals("the price structure price bands count", 1, priceStructure.getPriceBands().size());
        PriceBand priceBand = (PriceBand)priceStructure.getPriceBands().iterator().next();
        assertEquals("the price band price", new BigDecimal(5.0).doubleValue(), priceBand.getPrice().doubleValue(), 0.001);
    }

    /**
     * Tests the getAvailabilityForPerformances(Show) method.
     * @see BoxOfficeDao#getAvailabilityForPerformances(org.springframework.prospring.ticket.domain.Show)
     * @throws Exception
     */
    public void testGetAvailabilityForPerformancesWithNoBookings() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);
        dbHelper.insertPerformance(2, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, -1, 1);
        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);
        dbHelper.insertSeatStatus(2, 1, -1, 1);
        dbHelper.insertSeatStatus(2, 2, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);

        // executing the tested method.
        PerformanceWithAvailability[] performances = dao.getAvailabilityForPerformances(show);

        // assertions

        // there are no bookings in the system so all seats should be available.
        // which means that performance with id 1 should have 3 seats available, and
        // performance with id 2 should have 2 seats available.
        assertEquals("number of performances", 2, performances.length);
        for (int i=0; i<performances.length; i++) {
            PerformanceWithAvailability performance = performances[i];
            assertEquals("number of available seats", (performance.getId() == 1) ? 3 : 2, performance.getAvailableSeatCount());
            List priceBands = performance.getPriceBandWithAvailability();
            assertEquals("number of price bands with availability", 1, priceBands.size());
        }

    }

    /**
     * Tests the getAvailabilityForPerformances(Show) method. In this case, there is a booking in the system and
     * one of the seats in one of the performances is booked and reserved.
     * @see BoxOfficeDao#getAvailabilityForPerformances(org.springframework.prospring.ticket.domain.Show)
     * @throws Exception
     */
    public void testGetAvailabilityForPerformancesWithValidBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MINUTE, 5);
        Date fiveMinutesFromNow = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);
        dbHelper.insertPerformance(2, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        // a new booking in the system.
        dbHelper.insertBooking(1, 1, now, fiveMinutesFromNow, new BigDecimal(1.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        // now this seat status indicates that the seat is booked with
        // the new booking in the system. the reserved time of the new booking is still valid
        // thus the seat should not be available.
        dbHelper.insertSeatStatus(1, 1, 1, 1);

        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        dbHelper.insertSeatStatus(2, 1, -1, 1);
        dbHelper.insertSeatStatus(2, 2, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);

        // executing the tested method.
        PerformanceWithAvailability[] performances = dao.getAvailabilityForPerformances(show);

        // assertions
        assertEquals("number of performances", 2, performances.length);
        for (int i=0; i<performances.length; i++) {
            PerformanceWithAvailability performance = performances[i];
            assertEquals("number of available seats", 2, performance.getAvailableSeatCount());
            List priceBands = performance.getPriceBandWithAvailability();
            assertEquals("number of price bands with availability", 1, priceBands.size());
        }

    }

    /**
     * Tests the getAvailabilityForPerformances(Show) method. In this case, there is a booking in the system and
     * one of the seats in one of the performances is booked by the reservation has expired.
     * @see BoxOfficeDao#getAvailabilityForPerformances(org.springframework.prospring.ticket.domain.Show)
     * @throws Exception
     */
    public void testGetAvailabilityForPerformancesWithExpiredBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        Date fiveMinutesEarlier = cal.getTime();
        cal.add(Calendar.MINUTE, -5);
        Date tenMinutesEarlier = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);
        dbHelper.insertPerformance(2, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        // a new booking in the system.
        dbHelper.insertBooking(1, 1, tenMinutesEarlier, fiveMinutesEarlier, new BigDecimal(1.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        // now this seat status indicates that the seat is booked with
        // the new booking in the system. the reserved time of the booking has expired
        // thus the seat should be available.
        dbHelper.insertSeatStatus(1, 1, 1, 1);

        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        dbHelper.insertSeatStatus(2, 1, -1, 1);
        dbHelper.insertSeatStatus(2, 2, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);

        // executing the tested method.
        PerformanceWithAvailability[] performances = dao.getAvailabilityForPerformances(show);

        // assertions
        assertEquals("number of performances", 2, performances.length);
        for (int i=0; i<performances.length; i++) {
            PerformanceWithAvailability performance = performances[i];
            assertEquals("number of available seats", (performance.getId() == 1) ? 3 : 2, performance.getAvailableSeatCount());
            List priceBands = performance.getPriceBandWithAvailability();
            assertEquals("number of price bands with availability", 1, priceBands.size());
        }

    }

    /**
     * Tests the getAvailableSeats(Performance, SeatClass) method. In this case there are no bookings in
     * the system thus all seats should be available.
     * @see BoxOfficeDao#getAvailableSeats(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.SeatClass)
     * @throws Exception
     */
    public void testGetAvailableSeatsWithNoBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, -1, 1);
        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);
        PriceStructure priceStructure = new PriceStructure("ps");
        priceStructure.setId(1);
        Performance performance = new Performance(1, show, nextWeek, priceStructure);
        SeatClass seatClass = new SeatClass("code", "description");
        seatClass.setId(1);

        // executing the tested method.
        Seat[] seats = dao.getAvailableSeats(performance, seatClass);

        // assertions.
        assertEquals("number of available seats", 3, seats.length);
    }

    /**
     * Tests the getAvailableSeats(Performance, SeatClass) method. In this case there is a valid booking
     * the system thus one seat should not be available.
     * @see BoxOfficeDao#getAvailableSeats(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.SeatClass)
     * @throws Exception
     */
    public void testGetAvailableSeatsWithValidBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MINUTE, 5);
        Date fiveMinutesFromNow = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        // a new booking in the system.
        dbHelper.insertBooking(1, 1, now, fiveMinutesFromNow, new BigDecimal(1.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        // now this seat status indicates that the seat is booked with
        // the new booking in the system. the reserved time of the new booking is still valid
        // thus the seat should not be available.
        dbHelper.insertSeatStatus(1, 1, 1, 1);

        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);
        PriceStructure priceStructure = new PriceStructure("ps");
        priceStructure.setId(1);
        Performance performance = new Performance(1, show, nextWeek, priceStructure);
        SeatClass seatClass = new SeatClass("code", "description");
        seatClass.setId(1);

        // executing the tested method.
        Seat[] seats = dao.getAvailableSeats(performance, seatClass);

        // assertions.
        assertEquals("number of available seats", 2, seats.length);
        assertTrue("seats don't contain seat with id 1", seats[0].getId() != 1 && seats[1].getId() != 1);
    }

    /**
     * Tests the getAvailableSeats(Performance, SeatClass) method. In this case one of the seats is booked
     * but the booking has expired thus all seats should be available.
     * @see BoxOfficeDao#getAvailableSeats(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.SeatClass)
     * @throws Exception
     */
    public void testGetAvailableSeatsWithExpiredBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        Date fiveMinutesEarlier = cal.getTime();
        cal.add(Calendar.MINUTE, -5);
        Date tenMinutesEarlier = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        // a new booking in the system.
        dbHelper.insertBooking(1, 1, tenMinutesEarlier, fiveMinutesEarlier, new BigDecimal(1.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        // now this seat status indicates that the seat is booked with
        // the new booking in the system. the reserved time of the booking has expired
        // thus the seat should be available.
        dbHelper.insertSeatStatus(1, 1, 1, 1);

        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);
        PriceStructure priceStructure = new PriceStructure("ps");
        priceStructure.setId(1);
        Performance performance = new Performance(1, show, nextWeek, priceStructure);
        SeatClass seatClass = new SeatClass("code", "description");
        seatClass.setId(1);

        // executing the tested method.
        Seat[] seats = dao.getAvailableSeats(performance, seatClass);

        // assertions.
        assertEquals("number of available seats", 3, seats.length);
    }

    /**
     * Tests the getAvailableSeatsCount(Performance, SeatClass) method. In this case there are no bookings in
     * the system thus all seats should be available.
     * @see BoxOfficeDao#getAvailableSeats(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.SeatClass)
     * @throws Exception
     */
    public void testGetAvailableSeatsCountWithNoBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, -1, 1);
        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);
        PriceStructure priceStructure = new PriceStructure("ps");
        priceStructure.setId(1);
        Performance performance = new Performance(1, show, nextWeek, priceStructure);
        SeatClass seatClass = new SeatClass("code", "description");
        seatClass.setId(1);

        // executing the tested method.
        int count = dao.getAvailableSeatsCount(performance, seatClass);

        // assertions.
        assertEquals("number of available seats", 3, count);

    }

    /**
     * Tests the getAvailableSeatsCount(Performance, SeatClass) method. In this case there is a valid booking
     * the system thus one seat should not be available.
     * @see BoxOfficeDao#getAvailableSeatsCount(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.SeatClass)
     * @throws Exception
     */
    public void testGetAvailableSeatsCountWithValidBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.MINUTE, 5);
        Date fiveMinutesFromNow = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        // a new booking in the system.
        dbHelper.insertBooking(1, 1, now, fiveMinutesFromNow, new BigDecimal(1.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        // now this seat status indicates that the seat is booked with
        // the new booking in the system. the reserved time of the new booking is still valid
        // thus the seat should not be available.
        dbHelper.insertSeatStatus(1, 1, 1, 1);

        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);
        PriceStructure priceStructure = new PriceStructure("ps");
        priceStructure.setId(1);
        Performance performance = new Performance(1, show, nextWeek, priceStructure);
        SeatClass seatClass = new SeatClass("code", "description");
        seatClass.setId(1);

        // executing the tested method.
        int count = dao.getAvailableSeatsCount(performance, seatClass);

        // assertions.
        assertEquals("number of available seats", 2, count);
    }

    /**
     * Tests the getAvailableSeatsCount(Performance, SeatClass) method. In this case one of the seats is booked
     * but the booking has expired thus all seats should be available.
     * @see BoxOfficeDao#getAvailableSeatsCount(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.SeatClass)
     * @throws Exception
     */
    public void testGetAvailableSeatsCountWithExpiredBooking() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        Date fiveMinutesEarlier = cal.getTime();
        cal.add(Calendar.MINUTE, -5);
        Date tenMinutesEarlier = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = cal.getTime();

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);

        dbHelper.insertPriceStructure(1, "ps");

        dbHelper.insertSeatClass(1, "code", "desription");

        dbHelper.insertPerformance(1, nextWeek, 1, 1);

        dbHelper.insertPriceBand(1, new BigDecimal(1.0), 1, 1);

        // a new booking in the system.
        dbHelper.insertBooking(1, 1, tenMinutesEarlier, fiveMinutesEarlier, new BigDecimal(1.0), -1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        // now this seat status indicates that the seat is booked with
        // the new booking in the system. the reserved time of the booking has expired
        // thus the seat should be available.
        dbHelper.insertSeatStatus(1, 1, 1, 1);

        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the domain model.
        Show show = new Show("show");
        show.setId(1);
        PriceStructure priceStructure = new PriceStructure("ps");
        priceStructure.setId(1);
        Performance performance = new Performance(1, show, nextWeek, priceStructure);
        SeatClass seatClass = new SeatClass("code", "description");
        seatClass.setId(1);

        // executing the tested method.
        int count = dao.getAvailableSeatsCount(performance, seatClass);

        // assertions.
        assertEquals("number of available seats", 3, count);
    }

    /**
     * Tests the getCostOfSeats(Performance, Seat[]) method.
     * @see BoxOfficeDao#getCostOfSeats(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.Seat[])
     * @throws Exception
     */
    public void testGetCostOfSeats() throws Exception {

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);
        dbHelper.insertPerformance(1, new Date(), -1, 1);
        dbHelper.insertSeatClass(1, "code", "description");
        dbHelper.insertPriceBand(1, new BigDecimal(1.0), -1, 1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        dbHelper.insertSeatStatus(1, 1, -1, 1);
        dbHelper.insertSeatStatus(1, 2, -1, 1);
        dbHelper.insertSeatStatus(1, 3, -1, 1);

        // creating the domain model objects
        Show show = new Show("show");
        show.setId(1);
        Performance performance = new Performance(1, show, new Date(), null);
        Seat seat1 = new Seat("setat1");
        seat1.setId(1);
        Seat seat2 = new Seat("setat1");
        seat2.setId(2);
        Seat seat3 = new Seat("setat1");
        seat3.setId(3);
        seat1.setRightSide(seat2);
        seat2.setLeftSide(seat1);
        seat2.setRightSide(seat3);
        seat3.setLeftSide(seat2);
        Seat[] seats = new Seat[] { seat1, seat2, seat3 };

        // executing the tested method.
        BigDecimal cost = dao.getCostOfSeats(performance, seats);

        // assertions.
        assertEquals("the cost of the seats", 3.0, cost.doubleValue(), 0.001);
    }

    /**
     * Tests the removeBooking(Booking) method of the BoxOfficeDao.
     * @see BoxOfficeDao#removeBooking(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testRemoveBooking() throws Exception {

        // populating the database
        dbHelper.insertShow(1, "show", -1, -1);
        dbHelper.insertPerformance(1, new Date(), -1, 1);
        dbHelper.insertSeatClass(1, "code", "description");
        dbHelper.insertPriceBand(1, new BigDecimal(1.0), -1, 1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        Booking booking = new Booking(3, new Date(), new Date(), new BigDecimal(1.0));
        booking.setId(1);
        dbHelper.insertBooking(
            booking.getId(),
            booking.getSeatCount(),
            booking.getDateMade(),
            booking.getReservedUntil(),
            booking.getPrice(), -1);

        dbHelper.insertSeatStatus(1, 1, 1, 1);
        dbHelper.insertSeatStatus(1, 2, 1, 1);
        dbHelper.insertSeatStatus(1, 3, 1, 1);

        // executing the tested method
        dao.removeBooking(booking);

        beforeAssertions();

        // assertions
        assertTrue("no booking for seat status", dbHelper.verifyNoBookingReferenceInSeatStatus(1));
        assertTrue("no booking", dbHelper.verifyNoBooking(1));
    }

    /**
     * Tests the getBooking(id) method.
     * @see BoxOfficeDao#getBooking(long)
     * @throws Exception
     */
    public void testGetBooking() throws Exception {

        // inserting a booking to the database.
        Date dateMade = new Date();
        Date reserveUntil = new Date();
        dbHelper.insertBooking(1, 2, dateMade, reserveUntil, new BigDecimal(3.0), -1);

        // executing the tested method
        Booking booking = dao.getBooking(1);

        // assertions.
        assertNotNull("the dateMade of the booking", booking.getDateMade());
        assertNotNull("the reservedUntil of the booking", booking.getReservedUntil());
        assertEquals("the price of the booking", 3.0, booking.getPrice().doubleValue(), 0.001);
        assertEquals("the seat count of the booking", 2, booking.getSeatCount());
    }

    /**
     * Tests the getSeatsForBooking(Booking) method.
     * @see BoxOfficeDao#getSeatsForBooking(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testGetSeatsForBooking() throws Exception {

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);
        dbHelper.insertPerformance(1, new Date(), -1, 1);
        dbHelper.insertSeatClass(1, "code", "description");
        dbHelper.insertPriceBand(1, new BigDecimal(3.0), -1, 1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        Booking booking = new Booking(3, new Date(), new Date(), new BigDecimal(1.0));
        booking.setId(1);
        dbHelper.insertBooking(
            booking.getId(),
            booking.getSeatCount(),
            booking.getDateMade(),
            booking.getReservedUntil(),
            booking.getPrice(), -1);

        dbHelper.insertSeatStatus(1, 1, 1, 1);
        dbHelper.insertSeatStatus(1, 2, 1, 1);
        dbHelper.insertSeatStatus(1, 3, 1, 1);

        // exuecuting the tested method.
        List seats = dao.getSeatsForBooking(booking);

        // assertions.
        assertEquals("number of seats", 3, seats.size());
    }

    /**
     * Tests the getPerformanceForBooking(Booking) method.
     * @see BoxOfficeDao#getPerformanceForBooking(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testGetPerformanceForBooking() throws Exception {

        // populating the database.
        dbHelper.insertShow(1, "show", -1, -1);
        dbHelper.insertPerformance(1, new Date(), -1, 1);
        dbHelper.insertSeatClass(1, "code", "description");
        dbHelper.insertPriceBand(1, new BigDecimal(3.0), -1, 1);

        dbHelper.insertSeat(1, "seat1", -1, -1);
        dbHelper.insertSeat(2, "seat2", 1, -1);
        dbHelper.setRightSeatForSeat(1, 2);
        dbHelper.insertSeat(3, "seat3", 2, -1);
        dbHelper.setRightSeatForSeat(2, 3);

        Booking booking = new Booking(3, new Date(), new Date(), new BigDecimal(1.0));
        booking.setId(1);
        dbHelper.insertBooking(
            booking.getId(),
            booking.getSeatCount(),
            booking.getDateMade(),
            booking.getReservedUntil(),
            booking.getPrice(), -1);

        dbHelper.insertSeatStatus(1, 1, 1, 1);
        dbHelper.insertSeatStatus(1, 2, 1, 1);
        dbHelper.insertSeatStatus(1, 3, 1, 1);

        // exuecuting the tested method.
        Performance perforamnce = dao.getPerformanceForBooking(booking);

        // assertions.
        assertNotNull(perforamnce);
        assertNotNull("performance show not null", perforamnce.getShow());
    }

    //========================================= Helper Methods =================================================

    private void sortByProperty(List list, String propertyName) {
        Collections.sort(list, new BeanPropertyComparator(propertyName));
    }

    private class BeanPropertyComparator implements Comparator {

        private String propertyName;

        private Comparator comparator;

        public BeanPropertyComparator(String propertyName) {
            this(propertyName, null);
        }

        public BeanPropertyComparator(String propertyName, Comparator comparator) {
            this.propertyName = propertyName;
            this.comparator = comparator;
        }

        public int compare(Object o1, Object o2) {

            BeanWrapper wrapper1 = new BeanWrapperImpl(o1);
            Object value1 = wrapper1.getPropertyValue(propertyName);

            BeanWrapper wrapper2 = new BeanWrapperImpl(o2);
            Object value2 = wrapper2.getPropertyValue(propertyName);

            if (comparator != null) {
                return comparator.compare(value1, value2);
            }

            if ((value1 instanceof Comparable) && (value2 instanceof Comparable)) {
                return ((Comparable)value1).compareTo(value2);
            }

            // if the two values are not comparable then trying to compare their string representations.
            String str1;
            String str2;
            PropertyEditor editor = PropertyEditorManager.findEditor(wrapper1.getPropertyType(propertyName));
            if (editor != null) {
                editor.setValue(value1);
                str1 = editor.getAsText();
                editor.setValue(value2);
                str2 = editor.getAsText();
            } else {
                str1 = String.valueOf(value1);
                str2 = String.valueOf(value2);
            }
            return str1.compareTo(str2);

        }
    }

}
