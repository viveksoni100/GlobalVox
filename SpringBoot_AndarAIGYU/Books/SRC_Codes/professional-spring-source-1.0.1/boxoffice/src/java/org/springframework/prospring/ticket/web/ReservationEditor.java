package org.springframework.prospring.ticket.web;

import java.beans.*;

import org.springframework.prospring.ticket.service.*;
import org.springframework.prospring.ticket.domain.*;

/**
 * A PropertyEditor for Reservation.
 * Usually you would define property editors for persistent objects which load
 * them by their ids. In this case though, Reservation is not a persistent
 * object, it doesn't have an id, and cannot be loaded from the database.
 * Instaed, the editor construct a reservation object based on a booking
 * which is persistent.
 * This editor is based on the reservation booking id.
 */
public class ReservationEditor extends PropertyEditorSupport {

    private BoxOffice boxOffice;

    public ReservationEditor(BoxOffice boxOffice) {
        this.boxOffice = boxOffice;
    }

    /**
     * Sets the value of this editor.
     * @param bookingIdAsText A string representation of the booking id which the reservation will be created from.
     */
    public void setAsText(String bookingIdAsText) throws IllegalArgumentException {
        long bookingId = Long.valueOf(bookingIdAsText).longValue();

        // the following 2 instructions can be optimized to one call to the box office service
        // which in turn can use an optimized query on the database (one query instead of 3).
        // but we leave it for now, as it serves us well at this point.
        Booking booking = boxOffice.getBooking(bookingId);
        Seat[] seats = boxOffice.getBookedSeats(booking);

        // creating a reservation and setting it as the value of the editor.
        setValue(new Reservation(seats, booking));
    }

    /**
     * Returns a string representation of the Reservation held as the value of this editor.
     * The returned value is actually a string representation of the booking id the reservation
     * (the value of this editor) is based on.
     * @see #setAsText(String)
     * @return A string representation of the booking id the reservation is based on.
     */
    public String getAsText() {
        Reservation reservation = (Reservation)getValue();
        return String.valueOf(reservation.getBooking().getId());
    }

}
