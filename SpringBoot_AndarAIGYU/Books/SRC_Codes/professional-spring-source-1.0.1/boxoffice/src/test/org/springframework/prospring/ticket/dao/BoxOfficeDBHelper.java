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

import java.math.*;
import java.util.*;

import org.springframework.jdbc.core.*;
import org.springframework.prospring.ticket.domain.*;

/**
 * A Database Helper class used to manipulate the data in the database
 * for the dao tests. The goal of this class is to centralize all sql statements
 * in one class. This makes it easier to maintain when the db schema changes, and makes
 * the test classes more readable.
 *
 * @author Uri Boness
 */
public class BoxOfficeDBHelper {

    private JdbcTemplate jdbcTemplate;

    public BoxOfficeDBHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Clears the database. Deletes all data from all tables.
     */
    public void clearDatabase() {

        // removing all the internal references between the seats so it would be possible
        // to remove the seat records without constraint valiolation.
        jdbcTemplate.update("update Seat set left_Seat_id = null, right_Seat_id = null");

        jdbcTemplate.batchUpdate(new String[] {
            "delete from Seat_Status",
            "delete from Seat_Plan_Seat",
            "delete from Booking",
            "delete from Price_Band",
            "delete from Performance",
            "delete from Price_Structure",
            "delete from Shows",
            "delete from Genre",
            "delete from Seating_Plan",
            "delete from Seat_Class",
            "delete from Seat"
        });
    }

    /**
     * Inserts a new genre record to the Genre table.
     * @param id The id of the genre
     * @param name The name of the genre
     */
    public void insertGenre(long id, String name) {
        String sql = "insert into Genre (id, name) values (?, ?)";
        Object[] args = new Object[] { new Long(id), name };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a new show record to the Shows table.
     * @param id The id of the show.
     * @param name The name of the show.
     * @param genreId The id of the genre the show belongs to (-1 indicates null).
     * @param seatingPlanId The id of the default seating plan of the show (-1 indicates null).
     */
    public void insertShow(long id, String name, long genreId, long seatingPlanId) {
        String sql = "insert into Shows (id, name, Genre_id, Seating_Plan_id) values (?, ?, ?, ?)";
        Object[] args = new Object[] {
            new Long(id),
            name,
            (genreId == -1) ? null : new Long(genreId),
            (seatingPlanId == -1) ? null : new Long(seatingPlanId)
        };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a new peformance record to the Peformance database.
     * @param id The id of the performance.
     * @param dateAndTime The date of the performance.
     * @param priceStructureId The id of the price structure of the performance (-1 indicates null).
     * @param showId The id of the show the performance belongs to.
     */
    public void insertPerformance(long id, Date dateAndTime, long priceStructureId, long showId) {
        String sql = "insert into Performance (id, date_and_time, Price_Structure_id, Show_id) values (?, ?, ?, ?)";
        Object[] args = new Object[] {
            new Long(id),
            dateAndTime,
            (priceStructureId == -1) ? null : new Long(priceStructureId),
            new Long(showId)
        };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a price structure record to the Price_Structure table.
     * @param id The id of the price structure.
     * @param name The name of the price structure.
     */
    public void insertPriceStructure(long id, String name) {
        String sql = "insert into Price_Structure (id, name) values (?, ?)";
        Object[] args = new Object[] { new Long(id), name };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a price band record to the Price_Band table.
     * @param id The id of the price band.
     * @param price The price of the price band.
     * @param priceStructureId The price structure the price band belongs to (-1 represents null).
     * @param seatClassId The seat class of the price band (-1 represents null).
     */
    public void insertPriceBand(long id, BigDecimal price, long priceStructureId, long seatClassId) {
        String sql = "insert into Price_Band (id, price, Price_Structure_id, Seat_Class_id) values (?, ?, ?, ?)";
        Object[] args = new Object[] {
            new Long(id),
            price,
            (priceStructureId == -1) ? null : new Long(priceStructureId),
            (seatClassId == -1) ? null : new Long(seatClassId)
        };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a seat class record to the Seat_Class table.
     * @param id The id of the seat class.
     * @param code The code of the seat class.
     * @param description The description of the seat class.
     */
    public void insertSeatClass(long id, String code, String description) {
        String sql = "insert into Seat_Class (id, code, description) values (?, ?, ?)";
        Object[] args = new Object[] { new Long(id), code, description };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a new seat record to the Seat table.
     * @param id The id of the seat.
     * @param name The name of the seat.
     * @param leftSeatId The id of the left side seat (-1 indicates null).
     * @param rightSeatId The id of the right side seat (-1 indicates null).
     */
    public void insertSeat(long id, String name, long leftSeatId, long rightSeatId) {
        String sql = "insert into Seat (id, name, Left_Seat_id, Right_Seat_id) values (?, ?, ?, ?)";
        Object[] args = new Object[] {
            new Long(id),
            name,
            (leftSeatId == -1) ? null : new Long(leftSeatId),
            (rightSeatId == -1) ? null : new Long(rightSeatId)
        };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a new booking record to the Booking table.
     * @param id The id of the booking.
     * @param seatCount The number of seats reserved for the booking.
     * @param dateMade The date the booking was made.
     * @param reservedUntil The date till which the booking is reserved.
     * @param price The price of the booking.
     * @param purchaseId The id purchase of the booking (-1 indicates null).
     */
    public void insertBooking(long id, int seatCount, Date dateMade, Date reservedUntil, BigDecimal price, long purchaseId) {
        String sql = "insert into Booking (id, seat_count, date_made, reserved_until, price, Purchase_id) values (?, ?, ?, ?, ?, ?)";
        Object[] args = new Object[] {
            new Long(id),
            new Integer(seatCount),
            dateMade,
            reservedUntil,
            price,
            (purchaseId == -1) ? null : new Long(purchaseId)
        };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a new seat status record to the Seat_Status table.
     * @param performanceId The performance id.
     * @param seatId The seat id.
     * @param bookingId The booking id.
     * @param priceBandId The price band id.
     */
    public void insertSeatStatus(long performanceId, long seatId, long bookingId, long priceBandId) {
        String sql = "insert into Seat_Status (Performance_id, Seat_id, Booking_id, Price_Band_id) values (?, ?, ?, ?)";
        Object[] args = new Object[] {
            new Long(performanceId),
            new Long(seatId),
            (bookingId == -1) ? null : new Long(bookingId),
            new Long(priceBandId)
        };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Inserts a new seating plan record to the Seating_Plan table.
     * @param id The id of the seating plan.
     * @param name The name of the seating plan.
     */
    public void insertSeatingPlan(long id, String name) {
        String sql = "insert into Seating_Plan (id, name) values (?, ?)";
        Object[] args = new Object[] { new Long(id), name };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Returns the id of the booking record that holds the given values.
     * @param dateMade The date the booking was made.
     * @param reservedUntil The data till which the booking will be reserved.
     * @param price The price of the booking.
     * @return The id of the booking record that holds the given values.
     */
    public long getBookingId(Date dateMade, Date reservedUntil, BigDecimal price) {
        String sql = "select id from Booking where date_made = ? and reserved_until = ? and price = ?";
        Object[] args = new Object[] {
            dateMade,
            reservedUntil,
            price
        };
        return jdbcTemplate.queryForLong(sql, args);
    }

    /**
     * Verifies that given seat is booked with the given booking.
     * @param seatId The id of the given seat.
     * @param bookingId The id of the given booking.
     * @return True if the seat is booked with the given booking, false otherwise.
     */
    public boolean verifyBookingOfSeat(long seatId, long bookingId) {
        String sql = "select count(*) from Seat_Status where Seat_id = ? and Booking_id = ?";
        Object[] args = new Object[] { new Long(seatId), new Long(bookingId) };
        return jdbcTemplate.queryForInt(sql, args) > 0;
    }

    /**
     * Sets the right seat of the given seat.
     * @param seatId The id of the given seat.
     * @param rightSeatId The id of the right seat to be set.
     */
    public void setRightSeatForSeat(long seatId, long rightSeatId) {
        String sql = "update Seat set Right_Seat_id = ? where id = ?";
        Object[] args = new Object[] { new Long(rightSeatId) , new Long(seatId) };
        jdbcTemplate.update(sql, args);
    }

    /**
     * Returns the id of the purchase record that hold the given values.
     * @param authorizationCode The authorization code of the purchase.
     * @param purchaseDate The date of the purchase.
     * @param customerName The name of the customer who made the purchase.
     * @param encryptedCardNumber The encrypted card number that was used for the purchase.
     * @param email The email of the customer who made the purchase.
     * @param billingAddress The billing address of the purchase.
     * @param deliveryAddress The delivery address of the purchase.
     * @return The id of the purchase record that hold the given values.
     */
    public long getPurchaseId(
        String authorizationCode,
        Date purchaseDate,
        String customerName,
        String encryptedCardNumber,
        String email,
        Address billingAddress,
        Address deliveryAddress) {

        String sql = "select id from Purchase where " +
            "authorization_code = ? " +
            "and purchase_date = ? " +
            "and customer_name = ? " +
            "and encrypted_card_number = ? " +
            "and email = ? " +
            "and card_country = ? " +
            "and card_city = ? " +
            "and card_street = ? " +
            "and card_postcode = ? " +
            "and delivery_country = ? " +
            "and delivery_city = ? " +
            "and delivery_street = ? " +
            "and delivery_postcode = ?";

        Object[] args = new Object[] {
            authorizationCode,
            purchaseDate,
            customerName,
            encryptedCardNumber,
            email,
            billingAddress.getCountry(),
            billingAddress.getCity(),
            billingAddress.getStreet(),
            billingAddress.getPostcode(),
            deliveryAddress.getCountry(),
            deliveryAddress.getCity(),
            deliveryAddress.getStreet(),
            deliveryAddress.getPostcode()
        };

        return jdbcTemplate.queryForLong(sql, args);
    }

    /**
     * Verifies that the given booking is not reference by any seat status.
     * @param bookingId The id of the given booking.
     * @return True if the given booking is not referenced by any seat status, false otherwise.
     */
    public boolean verifyNoBookingReferenceInSeatStatus(long bookingId) {
        String sql = "select count(*) from Seat_Status where Booking_id = ?";
        Object[] args = new Object[] { new Long(bookingId) };
        int count = jdbcTemplate.queryForInt(sql, args);
        return count == 0;
    }

    /**
     * Verifies that there is no booking in the database with the given id.
     * @param bookingId The given booking id.
     * @return True if there is no booking in the database with the given id, false otherwise.
     */
    public boolean verifyNoBooking(long bookingId) {
        String sql = "select count(*) from Booking where id = ?";
        Object[] args = new Object[] { new Long(bookingId) };
        int count = jdbcTemplate.queryForInt(sql, args);
        return count == 0;
    }
}
