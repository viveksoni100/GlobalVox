package org.springframework.prospring.ticket.service;

import java.math.*;
import java.util.*;

import org.apache.commons.logging.*;
import org.springframework.prospring.ticket.dao.*;
import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.domain.support.*;
import org.springframework.prospring.ticket.service.payment.*;

/**
 * The default implementation of the BoxOffice service.
 */
public class BoxOfficeImpl implements BoxOffice {

    private final static Log logger = LogFactory.getLog(BoxOfficeImpl.class);

    // the dao this service uses to retrive data from the database.
    private BoxOfficeDao boxOfficeDao;

    // the payment processor used to process credit card payments.
    private PaymentProcessor paymentProcessor;

    /**
     * Returns a list of performances with available seats information for a given show.
     * @param show The given show.
     * @return A list of performances with available seats information for a given show. The list
     *         is sorted by the performance date.
     */
    public PerformanceWithAvailability[] getAvailabilityForPerformances(Show show) {
        PerformanceWithAvailability[] performances = boxOfficeDao.getAvailabilityForPerformances(show);
        Arrays.sort(performances, new Comparator() {
            public int compare(Object o1, Object o2) {
                Performance p1 = (Performance)o1;
                Performance p2 = (Performance)o2;
                return p1.getDateAndTime().compareTo(p2.getDateAndTime());
            }
        });
        return performances;
    }

    /**
     * Allocates seats for a performance. A reservation with a booking is created based on the given
     * reservation request. The booking is reserved for the user for some time to allow the user to complete
     * the payment process.
     * @param reservationRequest The given reservation request.
     * @return The newly created reservation.
     * @throws org.springframework.prospring.ticket.service.RequestedSeatNotAvailableException
     *          Thrown when requested sits are not available.
     * @throws org.springframework.prospring.ticket.service.NotEnoughSeatsException
     *          Thrown when there aren't enough sits to satisfy the given reuqest.
     */
    public Reservation allocateSeats(ReservationRequest reservationRequest) throws RequestedSeatNotAvailableException, NotEnoughSeatsException {
        Performance performance = reservationRequest.getPerformance();
        SeatClass seatClass = reservationRequest.getPriceBand().getSeatClass();

        Seat[] seats = boxOfficeDao.getAvailableSeats(performance, seatClass);
        if (seats.length < reservationRequest.getNumberOfSeatsRequested()) {
            throw new NotEnoughSeatsException(seatClass.getId(), seats.length, reservationRequest.getNumberOfSeatsRequested());
        }

        Seat[] seatsRequested = selectSeatsFromAvailableSeats(seats, reservationRequest.getNumberOfSeatsRequested());

        BigDecimal price = boxOfficeDao.getCostOfSeats(performance, seatsRequested);

        // New booking
        Booking booking = createBooking(
            reservationRequest.getNumberOfSeatsRequested(),
            reservationRequest.getBookingFee().add(price),
            reservationRequest.getHoldUntil()
        );

        // Add booking and update seat status
        boxOfficeDao.reserveSeats(seatsRequested, performance, booking);

        // Create a Reservation object
        return new Reservation(seatsRequested, booking);
    }

    /**
     * Performs the tickets purchase based on the given purchase request.
     * @param purchaseRequest The given purchase request.
     * @return The successful purchase that was made.
     * @throws org.springframework.prospring.ticket.service.payment.PaymentException
     *          Thorwn if encountered any errors during the payment process.
     * @throws org.springframework.prospring.ticket.service.RequestedSeatNotAvailableException
     *          Thrown if the requested seats were not
     *          available anymore at the time of the purchase.
     */
    public Purchase purchaseTickets(PurchaseRequest purchaseRequest)
        throws PaymentException, CreditCardValidationException, RequestedSeatNotAvailableException {

        // first we validate the purchase request
        paymentProcessor.validate(purchaseRequest);

        // checking if the seats are still available
        Booking booking = purchaseRequest.getReservation().getBooking();
        if (!areSeatsStillAvailable(booking)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not purchase tickets for booked seats are no longer available; booking #" +
                    booking.getId());
            }
            throw new RequestedSeatNotAvailableException("Seats no longer available", purchaseRequest.getPerformance());
        }

        Purchase purchase = createPurchase();
        purchase.setBillingAddress(purchaseRequest.getBillingAddress());
        Address deliveryAddress = purchaseRequest.getDeliveryAddress();
        if (deliveryAddress == null) {
            deliveryAddress = purchaseRequest.getBillingAddress();
        }

        purchase.setPaymentAuthorizationCode("");
        purchase.setDeliveryAddress(deliveryAddress);
        purchase.setEmail(purchaseRequest.getEmail());
        purchase.setPurchaseDate(new Date());
        purchase.setCustomerName(purchaseRequest.getCreditCardDetails().getNameOnCard());
        purchase.setEncryptedCardNumber(purchaseRequest.getCreditCardDetails().getEncryptedCardNumber());
        purchase.setUserCollected(purchaseRequest.isCollect());
        booking.setPurchase(purchase);
        boxOfficeDao.savePurchaseForBooking(booking);

        String code = paymentProcessor.process(purchaseRequest);
        purchase.setPaymentAuthorizationCode(code);
        boxOfficeDao.updatePurchaseAuthorizationCode(purchase);

        return purchase;
    }

    /**
     * Check whether the seats associated with the given booking are still available.
     * @param booking The given booking.
     * @return True if the seats of the booking are still availabe, false otherwise.
     */
    public boolean areSeatsStillAvailable(Booking booking) {
        return boxOfficeDao.checkAvailabilityForBooking(booking);
    }

    /**
     * Returns the available seats of the given seat class in the given performance.
     * @param performance The given performance.
     * @param seatClass   The class of the requested seats.
     * @return The available seats of the given seat class in the given performance.
     */
    public Seat[] getAvailableSeats(Performance performance, SeatClass seatClass) {
        return boxOfficeDao.getAvailableSeats(performance, seatClass);
    }

    /**
     * Returns the number of available seats of the given seat class in the given performance.
     * @param performance The given performance.
     * @param seatClass The given seat class.
     * @return The number of available seats of the given seat class in the given performance.
     */
    public int getAvailableSeatsCount(Performance performance, SeatClass seatClass) {
        return boxOfficeDao.getAvailableSeatsCount(performance, seatClass);
    }

    /**
     * Returns the price band associated with the given id.
     * @param priceBandId The id of the requested price band.
     * @return The price band associated with the given id.
     */
    public PriceBand getPriceBand(long priceBandId) {
        return boxOfficeDao.getPriceBand(priceBandId);
    }

    /**
     * Cancels the given booking.
     * @param booking The booking to be canceled.
     */
    public void cancelBooking(Booking booking) {
        boxOfficeDao.removeBooking(booking);
    }

    /**
     * Returns the booking identified by the given id.
     * @param bookingId The id of the requested booking.
     * @return The booking identified by the given id.
     */
    public Booking getBooking(long bookingId) {
        return boxOfficeDao.getBooking(bookingId);
    }

    /**
     * Returns the booked seats of the given booking.
     * @param booking The given booking.
     * @return The booked seats of the given booking.
     */
    public Seat[] getBookedSeats(Booking booking) {
        List seats = boxOfficeDao.getSeatsForBooking(booking);
        return (Seat[])seats.toArray(new Seat[seats.size()]);
    }

    /**
     * Returns the performance of the given booking.
     * @param booking The given booking.
     * @return The performance of the given booking.
     */
    public Performance getPerformanceOfBooking(Booking booking) {
        return boxOfficeDao.getPerformanceForBooking(booking);
    }

    //===================================== Setter/Getter for DI ========================================

    /**
     * Sets the box office dao this service will use to access the database.
     * @param boxOfficeDao The box office dao this service will use to access the database.
     */
    public void setBoxOfficeDao(BoxOfficeDao boxOfficeDao) {
        this.boxOfficeDao = boxOfficeDao;
    }

    /**
     * Sets the payment processor this service will use to process payment requests.
     * @param paymentProcessor The payment processor this service will use to process payment requests.
     */
    public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    //============================== Helper Methods (useful for testing) =================================

    /**
     * Selects the given number of seast from the given list of available seats.
     * In realworld application it would probably be wiser to have a strategy interface for this task, and
     * have different algorithms for seat selection.
     * @param availableSeats The list of available seats.
     * @param numberOfSeats The number of seats to select.
     * @return The selected seats.
     */
    protected Seat[] selectSeatsFromAvailableSeats(Seat[] availableSeats, int numberOfSeats) {
        Seat[] seatsRequested = new Seat[numberOfSeats];
        System.arraycopy(availableSeats, 0, seatsRequested, 0, numberOfSeats);
        return seatsRequested;
    }

    /**
     * Creates a new booking from the given values.
     * @param price The price of the booking
     * @param holdUntil The date on which the booking will expire having that the purchase wasn't made.
     * @return The newly created booking.
     */
    protected Booking createBooking(int seatCount, BigDecimal price, Date holdUntil) {
        return new Booking(seatCount, holdUntil, price);
    }

    /**
     * Creates a new Purchase.
     * @return The newly created purchase.
     */
    protected Purchase createPurchase() {
        return new Purchase();
    }


}
