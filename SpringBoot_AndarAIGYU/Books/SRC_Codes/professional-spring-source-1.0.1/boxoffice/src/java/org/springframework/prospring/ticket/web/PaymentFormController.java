package org.springframework.prospring.ticket.web;

import java.text.*;
import java.util.*;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.springframework.beans.propertyeditors.*;
import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.prospring.ticket.service.payment.*;
import org.springframework.prospring.ticket.web.confirmation.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.*;
import org.springframework.web.util.*;

/**
 * The form controller for the "Payment Form" screen.
 *
 * @author Uri Boness
 */
public class PaymentFormController extends SimpleFormController {

    private final static Log logger = LogFactory.getLog(PaymentFormController.class);

    // The date format for the credit card expiry date.
    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MM/yy");

    // the box office service this controller uses.
    private BoxOffice boxOffice;

    // the sender that will be used to send purchase confirmation mails.
    private PurchaseConfirmationSender confirmationSender;

    // the name of the "seatsGone" view.
    private String seatsGoneViewName;

    /**
     * Registering property editors for the form fields
     * @see BaseCommandController#initBinder(
     *      javax.servlet.http.HttpServletRequest,
     *      org.springframework.web.bind.ServletRequestDataBinder)
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(DATE_FORMAT, false));
        binder.registerCustomEditor(Reservation.class, new ReservationEditor(boxOffice));
    }

    /**
     * Creates the PurchaseRequest that serves as the form backing object for the form.
     * @see AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Reservation reservation = loadReservation(request);
        Performance performance = loadReservationRequest(request).getPerformance();
        return createPurchaseRequest(reservation, performance);
    }

    /**
     * Checking if the seats for the booking are still available before the form is show. If not, then
     * redirecting to "seatsGone" view.
     */
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        Reservation reservation = loadReservation(request);
        Performance performance = loadReservationRequest(request).getPerformance();
        if (!boxOffice.areSeatsStillAvailable(reservation.getBooking())) {
            ModelAndView mav = new ModelAndView(seatsGoneViewName);
            mav.addObject("performance", performance);
            return mav;
        }
        return super.showForm(request, response, errors);
    }

    /**
     * Called on form submition (after the form backing object was created,
     * initialized (by the binder), and binded and validated)
     *
     * @see SimpleFormController#onSubmit(
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse,
     *      Object,
     *      org.springframework.validation.BindException)
     */
    protected ModelAndView onSubmit(
        HttpServletRequest request,
        HttpServletResponse response,
        Object command,
        BindException errors) throws Exception, RequestedSeatNotAvailableException {

        PurchaseRequest purchaseRequest = (PurchaseRequest)command;

        Reservation reservation = getAndRemoveReservationFromSession(request);

        // if the reservation is null then this is a resubmission of the form in which case
        // we don't want to perform the purchase again.
        if (reservation == null) {
            return handleResubmission(purchaseRequest);
        }

        // doing the purchase.
        Purchase purchase;
        try {

            purchase = boxOffice.purchaseTickets(purchaseRequest);

        } catch (CreditCardValidationException ccve) {
            keepReservation(request, reservation); // putting the reservation back in the session.
            errors.reject(ccve.getErrorCode());
            return showForm(request, errors, getFormView());
        } catch (PaymentException pe) {
            keepReservation(request, reservation); // putting the reservation back in the session.
            errors.reject(pe.getErrorCode());
            return showForm(request, errors, getFormView());
        }

        // sending the confirmation email.
        try {
            confirmationSender.sendPurchaseConfirmation(purchase, purchaseRequest);
        } catch (PurchaseConfirmationSendingException pcse) {
            logger.error("Could not send confirmation email for purchase with id #" + purchase.getId(), pcse);
        }

        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("useBillingAddressForDelivery", shouldUseBillingAddressForDelivery(purchase));
        mav.addObject("reservation", purchaseRequest.getReservation());
        mav.addObject("performance", purchaseRequest.getPerformance());
        mav.addObject("purchase", purchase);
        return mav;
    }

    /**
     * Handles a resubmission of the payment form.
     * @param purchaseRequest The purchase request.
     * @return The modle and view to display the purchase confirmation.
     */
    protected ModelAndView handleResubmission(PurchaseRequest purchaseRequest) {
        Purchase purchase = purchaseRequest.getReservation().getBooking().getPurchase();
        ModelAndView mav = new ModelAndView(getSuccessView());
        mav.addObject("useBillingAddressForDelivery", shouldUseBillingAddressForDelivery(purchase));
        mav.addObject("reservation", purchaseRequest.getReservation());
        mav.addObject("performance", purchaseRequest.getPerformance());
        mav.addObject("purchase", purchase);

        return mav;
    }

    /**
     * Sets the box office service this controller uses.
     * @param boxOffice The box office service this controller uses.
     */
    public void setBoxOffice(BoxOffice boxOffice) {
        this.boxOffice = boxOffice;
    }

    /**
     * Sets the purchase confirmation sender this controller will use.
     * @param confirmationSender The purchase confirmation sender this controller will use.
     */
    public void setConfirmationSender(PurchaseConfirmationSender confirmationSender) {
        this.confirmationSender = confirmationSender;
    }

    /**
     * Sets the name of the "Seats Gone" view.
     * @param seatsGoneViewName The name of the "Seats Gone" view.
     */
    public void setSeatsGoneViewName(String seatsGoneViewName) {
        this.seatsGoneViewName = seatsGoneViewName;
    }

    //========================================= Helper Methods ================================================

    /**
     * Determines whether the billing address should be used as the delivery address.
     * @param purchase The relevant purchase.
     * @return True if the billing address should serve for delivery, false otherwise.
     */
    protected Boolean shouldUseBillingAddressForDelivery(Purchase purchase) {
        Address address = purchase.getDeliveryAddress();
        if (address == null) {
            return Boolean.TRUE;
        }
        return (address.getCountry() == null || address.getCountry().length() == 0)? Boolean.TRUE : Boolean.FALSE;
    }

    // loads the reservation from the request.
    protected Reservation loadReservation(HttpServletRequest request) {
        return (Reservation)WebUtils.getRequiredSessionAttribute(request, "reservation");
    }

    // loads the performance from the request.
    protected ReservationRequest loadReservationRequest(HttpServletRequest request) {
        return (ReservationRequest)WebUtils.getRequiredSessionAttribute(request, "reservationRequest");
    }

    // puts the given reservation back in the session.
    protected void keepReservation(HttpServletRequest request, Reservation reservation) {
        WebUtils.setSessionAttribute(request, "reservation", reservation);
    }

    // puts the given performance back in the session.
    protected void keepPerformance(HttpServletRequest request, Performance performance) {
        WebUtils.setSessionAttribute(request, "performance", performance);
    }

    // creates a new purchase request with the given reservation
    protected PurchaseRequest createPurchaseRequest(Reservation reservation, Performance performance) {
        PurchaseRequest request = new PurchaseRequest();
        request.setBillingAddress(new Address());
        request.setDeliveryAddress(new Address());
        request.setCreditCardDetails(new CreditCardDetails());
        request.setReservation(reservation);
        request.setPerformance(performance);
        return request;
    }

    // retrieves and removes the reservation from the session.
    protected Reservation getAndRemoveReservationFromSession(HttpServletRequest request) {
        Reservation reservation = (Reservation)WebUtils.getSessionAttribute(request, "reservation");
        request.getSession(true).removeAttribute("reservation");
        return reservation;
    }

    // retrieves and removes the reservation request from the session.
    protected ReservationRequest getAndRemoveReservationRequestFromSession(HttpServletRequest request) {
        ReservationRequest reservationRequest = (ReservationRequest)WebUtils.getSessionAttribute(request, "reservationRequest");
        request.getSession(true).removeAttribute("reservationRequest");
        return reservationRequest;
    }

}
