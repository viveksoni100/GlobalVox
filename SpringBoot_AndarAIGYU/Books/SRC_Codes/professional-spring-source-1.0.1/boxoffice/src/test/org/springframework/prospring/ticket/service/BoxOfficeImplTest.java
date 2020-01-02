package org.springframework.prospring.ticket.service;

import java.util.*;
import java.math.*;

import junit.framework.*;
import org.easymock.*;
import org.springframework.prospring.ticket.dao.*;
import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.domain.support.*;
import org.springframework.prospring.ticket.service.payment.*;

/**
 * Tests the BoxOfficImpl service.
 *
 * @see BoxOfficeImpl
 *
 * @author Uri Boness
 */
public class BoxOfficeImplTest extends TestCase {

    // the box office implementation under test.
    private BoxOfficeImpl boxOffice;

    // mocking the BoxOfficeDao.
    private BoxOfficeDao daoMock;
    private MockControl daoMockControl;

    // mocking the payment processor.
    private PaymentProcessor paymentProcessorMock;
    private MockControl paymentProcessorMockControl;

    protected void setUp() throws Exception {

        // creating the dao mock.
        daoMockControl = MockControl.createControl(BoxOfficeDao.class);
        daoMock = (BoxOfficeDao)daoMockControl.getMock();

        // creating the payment processor mock.
        paymentProcessorMockControl = MockControl.createControl(PaymentProcessor.class);
        paymentProcessorMock = (PaymentProcessor)paymentProcessorMockControl.getMock();

        // initializing the box office and setting its dependecies with mocks.
        boxOffice = new BoxOfficeImpl();
        initBoxOffice(boxOffice);
    }

    /**
     * Tests the getAvailabilityForPerformances(Show) method on the BoxOffice service.
     * @see BoxOffice#getAvailabilityForPerformances(org.springframework.prospring.ticket.domain.Show)
     * @throws Exception
     */
    public void testGetAvailabilityForPerformances() throws Exception {
        Show show = new Show("show");

        // the expteced result from the method call. The result itself doesn't have to
        // be of a real value. This is where the mocking shows its power. Because the
        // dao is mocked, we don't really care what the dao returns when its methods are called.
        // What we really want to test is what the service (the boxOffice) does with those return values.
        // in this case the service just returns what the doa returns. All we need to do is to make sure
        // this behavior take place.
        PerformanceWithAvailability[] expectedResult = new PerformanceWithAvailability[0];

        // "recording" the expectations from the dao.
        daoMock.getAvailabilityForPerformances(show);
        daoMockControl.setReturnValue(expectedResult);

        // stopping the "recording" and now waiting for expectations to be fulfilled.
        daoMockControl.replay();

        PerformanceWithAvailability[] result = boxOffice.getAvailabilityForPerformances(show);

        // asserting that the returned result is the same is returned from the dao.
        assertSame(expectedResult, result);

        // verifying the expectations were met.
        daoMockControl.verify();
    }

    /**
     * Tests the allocateSeats(ReservationRequest) method on the BoxOffice service.
     * @see BoxOffice#allocateSeats(ReservationRequest)
     * @throws Exception
     */
    public void testAllocateSeats() throws Exception {

        // creating the data objects.
        Show show = new Show("show");
        Performance performance = new Performance(show, new Date(), null);
        SeatClass seatClass = new SeatClass("code", "description");
        PriceBand priceBand = new PriceBand(seatClass, new BigDecimal(1.0));
        ReservationRequest request = new ReservationRequest(performance, priceBand, 2, 5);
        request.setBookingFee(new BigDecimal(1.0));

        Seat seat1 = new Seat("seat1");
        Seat seat2 = new Seat("seat2");
        Seat seat3 = new Seat("seat3");
        final Seat[] seats = new Seat[] { seat1, seat2, seat3 };

        // setting the expectations from the dao.
        daoMock.getAvailableSeats(performance, seatClass);
        daoMockControl.setReturnValue(seats);

        final Seat[] seatsToReserve = new Seat[] { seat1, seat2 };

        daoMock.getCostOfSeats(performance, seatsToReserve);
        daoMockControl.setReturnValue(new BigDecimal(2.0));

        final Booking booking = new Booking();
        daoMock.reserveSeats(seatsToReserve, performance, booking);

        // we create a new box office here for a reason. Sometimes objects that are created within a the tested
        // methods are hard to mock. The easiest solution to that is to extract the creation of those object to
        // protected methods within the tested class and then override them to control what object they return.
        boxOffice = new BoxOfficeImpl() {
            protected Seat[] selectSeatsFromAvailableSeats(Seat[] availableSeats, int numberOfSeats) {
                assertSame(seats, availableSeats);
                assertEquals("number of seats to reserve", 2, numberOfSeats);
                return seatsToReserve;
            }

            protected Booking createBooking(int seatCount, BigDecimal price, Date holdUntil) {
                assertEquals("price of booking", 2.0 + 1.0, price.doubleValue(), 0.001);
                assertEquals("seat count", 2, seatCount);
                return booking;
            }
        };
        boxOffice.setBoxOfficeDao(daoMock);

        daoMockControl.replay();

        // exectuing the tested method.
        Reservation reservation = boxOffice.allocateSeats(request);

        // assertions and verifications
        assertSame(booking, reservation.getBooking());
        assertSame(seatsToReserve, reservation.getSeats());

        daoMockControl.verify();
    }

    /**
     * Tests the purchaseTickets(PurchaseRequest) method of the BoxOffice service.
     * @see BoxOffice#purchaseTickets(PurchaseRequest)
     * @throws Exception
     */
    public void testPurchaseTickets() throws Exception {

        // creating the data objects
        Seat[] seats = new Seat[0];
        Booking booking = new Booking();
        Reservation reservation = new Reservation(seats, booking);

        Address address = new Address("country", "city", "street", "123");
        CreditCardDetails cardDetails = new CreditCardDetails("visa", "john", "1234-1234-1234-1234", new Date());

        PurchaseRequest request = new PurchaseRequest();
        request.setReservation(reservation);
        request.setEmail("email");
        request.setDeliveryAddress(address);
        request.setBillingAddress(address);
        request.setCollect(true);
        request.setCreditCardDetails(cardDetails);

        // setting the expectations
        paymentProcessorMock.validate(request);

        paymentProcessorMock.process(request);
        paymentProcessorMockControl.setReturnValue("ABC");

        daoMock.checkAvailabilityForBooking(booking);
        daoMockControl.setReturnValue(true);

        daoMock.savePurchaseForBooking(booking);

        final Purchase expectedPurchase = new Purchase();
        daoMock.updatePurchaseAuthorizationCode(expectedPurchase);

        paymentProcessorMockControl.replay();
        daoMockControl.replay();

        // executing the tested method
        BoxOfficeImpl boxOffice = new BoxOfficeImpl() {
            protected Purchase createPurchase() {
                return expectedPurchase;
            }
        };
        initBoxOffice(boxOffice);
        Purchase purchase = boxOffice.purchaseTickets(request);

        // assertions and verifications
        assertNotNull(purchase);
        assertEquals("the purchase billing address", address, purchase.getBillingAddress());
        assertEquals("the purchase delivery address", address, purchase.getDeliveryAddress());
        assertEquals("the purchase customer name", cardDetails.getNameOnCard(), purchase.getCustomerName());
        assertEquals("the purchase email", "email", purchase.getEmail());
        assertEquals("the purchase encrypted card number", cardDetails.getEncryptedCardNumber(),
            purchase.getEncryptedCardNumber());
        assertEquals("the purchase authorization code", "ABC", purchase.getPaymentAuthorizationCode());

        paymentProcessorMockControl.verify();
        daoMockControl.verify();
    }

    /**
     * Tests the purchaseTickets(PurchaseRequest) method of the BoxOffice service. In this case some seats
     * are no longer available.
     * @see BoxOffice#purchaseTickets(PurchaseRequest)
     * @throws Exception
     */
    public void testPurchaseTicketsWithUnavailableSeats() throws Exception {

        // creating the data objects
        Seat[] seats = new Seat[0];
        Booking booking = new Booking();
        Reservation reservation = new Reservation(seats, booking);

        Address address = new Address("country", "city", "street", "123");
        CreditCardDetails cardDetails = new CreditCardDetails("visa", "john", "1234-1234-1234-1234", new Date());

        PurchaseRequest request = new PurchaseRequest();
        request.setReservation(reservation);
        request.setEmail("email");
        request.setDeliveryAddress(address);
        request.setBillingAddress(address);
        request.setCollect(true);
        request.setCreditCardDetails(cardDetails);

        // setting the expectations
        paymentProcessorMock.validate(request);

        daoMock.checkAvailabilityForBooking(booking);
        daoMockControl.setReturnValue(false);

        paymentProcessorMockControl.replay();
        daoMockControl.replay();

        // executing the tested method
        try {
            boxOffice.purchaseTickets(request);
            fail("An RequestedSeatNotAvailableException should be thrown if there are seats in the booking that are no" +
                "longer available");
        } catch (RequestedSeatNotAvailableException rsnae) {
            // expected behavior.
        }

        paymentProcessorMockControl.verify();
        daoMockControl.verify();
    }

    /**
     * Tests the purchaseTickets(PurchaseRequest) method of the BoxOffice service. In this case the payment
     * process fails.
     * @see BoxOffice#purchaseTickets(PurchaseRequest)
     * @throws Exception
     */
    public void testPurchaseTicketsWithPaymentException() throws Exception {

        // creating the data objects
        Seat[] seats = new Seat[0];
        Booking booking = new Booking();
        Reservation reservation = new Reservation(seats, booking);

        Address address = new Address("country", "city", "street", "123");
        CreditCardDetails cardDetails = new CreditCardDetails("visa", "john", "1234-1234-1234-1234", new Date());

        PurchaseRequest request = new PurchaseRequest();
        request.setReservation(reservation);
        request.setEmail("email");
        request.setDeliveryAddress(address);
        request.setBillingAddress(address);
        request.setCollect(true);
        request.setCreditCardDetails(cardDetails);

        // setting the expectations
        paymentProcessorMock.validate(request);

        paymentProcessorMock.process(request);
        paymentProcessorMockControl.setThrowable(new PaymentException("badCredit"));

        daoMock.checkAvailabilityForBooking(booking);
        daoMockControl.setReturnValue(true);

        daoMock.savePurchaseForBooking(booking);

        paymentProcessorMockControl.replay();
        daoMockControl.replay();

        // executing the tested method
        try {
            boxOffice.purchaseTickets(request);
            fail("A PaymentException should be thrown by the payment processor");
        } catch (PaymentException pe) {
            // expected behavior.
        }

        paymentProcessorMockControl.verify();
        daoMockControl.verify();
    }

    /**
     * Tests the purchaseTickets(PurchaseRequest) method of the BoxOffice service. In this case the payment
     * validation fails.
     * @see BoxOffice#purchaseTickets(PurchaseRequest)
     * @throws Exception
     */
    public void testPurchaseTicketsWithInvalidPayment() throws Exception {

        // creating the data objects
        Seat[] seats = new Seat[0];
        Booking booking = new Booking();
        Reservation reservation = new Reservation(seats, booking);

        Address address = new Address("country", "city", "street", "123");
        CreditCardDetails cardDetails = new CreditCardDetails("visa", "john", "1234-1234-1234-1234", new Date());

        PurchaseRequest request = new PurchaseRequest();
        request.setReservation(reservation);
        request.setEmail("email");
        request.setDeliveryAddress(address);
        request.setBillingAddress(address);
        request.setCollect(true);
        request.setCreditCardDetails(cardDetails);

        // setting the expectations
        paymentProcessorMock.validate(request);
        paymentProcessorMockControl.setThrowable(new CreditCardValidationException("badCreditCard"));

        paymentProcessorMockControl.replay();

        // executing the tested method
        try {
            boxOffice.purchaseTickets(request);
            fail("A CreditCardValidationException should be thrown by the payment processor");
        } catch (CreditCardValidationException ccve) {
            // expected behavior.
        }

        paymentProcessorMockControl.verify();
    }

    /**
     * Tests the getAvailableSeats(Performance, SeatClass) method in BoxOffice service.
     * @see BoxOffice#getAvailableSeats(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.SeatClass)
     * @throws Exception
     */
    public void testGetAvailableSeats() throws Exception {

        // creating the data objects.
        Performance performance = new Performance(null, new Date(), null);
        SeatClass seatClass = new SeatClass("code", "description");

        // setting the expectations.
        Seat[] expectedResult = new Seat[0];
        daoMock.getAvailableSeats(performance, seatClass);
        daoMockControl.setReturnValue(expectedResult);

        daoMockControl.replay();

        // executing the tested method.
        Seat[] result = boxOffice.getAvailableSeats(performance, seatClass);

        // assertions and verifications.
        assertSame(expectedResult, result);
        daoMockControl.verify();

    }

    /**
     * Tests the getAvailableSeatsCount(Performance, SeatClass) method in BoxOffice service.
     * @see BoxOffice#getAvailableSeatsCount(
     *          org.springframework.prospring.ticket.domain.Performance,
     *          org.springframework.prospring.ticket.domain.SeatClass)
     * @throws Exception
     */
    public void testGetAvailableSeatsCount() throws Exception {

        // creating the data objects.
        Performance performance = new Performance(null, new Date(), null);
        SeatClass seatClass = new SeatClass("code", "description");

        // setting the expectations.
        daoMock.getAvailableSeatsCount(performance, seatClass);
        daoMockControl.setReturnValue(3);

        daoMockControl.replay();

        // executing the tested method.
        int count = boxOffice.getAvailableSeatsCount(performance, seatClass);

        // assertions and verifications.
        assertEquals(3, count);
        daoMockControl.verify();

    }

    /**
     * Tests the getPriceBand(id) of the BoxOffice service.
     * @see BoxOffice#getPriceBand(long)
     * @throws Exception
     */
    public void testGetPriceBand() throws Exception {

        // setting the expectations.
        PriceBand expectedResult = new PriceBand(null, null);
        daoMock.getPriceBand(1);
        daoMockControl.setReturnValue(expectedResult);

        daoMockControl.replay();

        // executing the tested method.
        PriceBand result = boxOffice.getPriceBand(1);

        // assertions and verifications.
        assertSame(expectedResult, result);
        daoMockControl.verify();
    }

    /**
     * Tests the cancelBooking(Booking) method of the BoxOffice service.
     * @see BoxOffice#cancelBooking(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testCancelBooking() throws Exception {

        // setting the expectations.
        Booking booking = new Booking();
        daoMock.removeBooking(booking);

        daoMockControl.replay();

        // executing the tested method.
        boxOffice.cancelBooking(booking);

        // assertions & verifications
        daoMockControl.verify();
    }

    /**
     * Tests the getBooking(id) of the BoxOffice service.
     * @see BoxOffice#getBooking(long)
     * @throws Exception
     */
    public void testGetBooking() throws Exception {

        // setting the expectations.
        Booking booking = new Booking();
        daoMock.getBooking(1);
        daoMockControl.setReturnValue(booking);

        daoMockControl.replay();

        // executing the tested method.
        Booking result = boxOffice.getBooking(1);

        // assertions and verifications.
        assertSame(booking, result);
        daoMockControl.verify();
    }

    /**
     * Tests the getBookedSeats(Booking) of the BoxOffice service.
     * @see BoxOffice#getBookedSeats(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testGetBookedSeats() throws Exception {

        // setting the expectations.
        Booking booking = new Booking();
        List seats = new ArrayList();
        seats.add(new Seat());
        seats.add(new Seat());
        daoMock.getSeatsForBooking(booking);
        daoMockControl.setReturnValue(seats);

        daoMockControl.replay();

        // executing the tested method.
        Seat[] result = boxOffice.getBookedSeats(booking);

        // assertions and verifications.
        assertEquals("number of seats", seats.size(), result.length);
        daoMockControl.verify();
    }

    /**
     * Tests the getPerformanceOfBooking(Booking) method of the BoxOffice service.
     * @see BoxOffice#getPerformanceOfBooking(org.springframework.prospring.ticket.domain.Booking)
     * @throws Exception
     */
    public void testGetPerformanceOfBooking() throws Exception {
        // setting the expectations.
        Booking booking = new Booking();
        Performance performance = new Performance();
        daoMock.getPerformanceForBooking(booking);
        daoMockControl.setReturnValue(performance);

        daoMockControl.replay();

        // executing the tested method.
        Performance result = boxOffice.getPerformanceOfBooking(booking);

        // assertions and verifications.
        assertSame(performance, result);
        daoMockControl.verify();
    }

    // initializes the box office with its dependencies.
    private void initBoxOffice(BoxOfficeImpl boxOffice) {
        boxOffice.setBoxOfficeDao(daoMock);
        boxOffice.setPaymentProcessor(paymentProcessorMock);
    }
}