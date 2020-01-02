package org.springframework.prospring.ticket.web.confirmation;

import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.mail.*;

/**
 * Sends a simple text email message as a purchase confirmation.
 */
public class SimpleMailPurchaseConfirmationSender implements PurchaseConfirmationSender {

    // the mail sender that will be used to send the emails.
    private MailSender mailSender;

    // the email of the sender of the confirmation emails.
    private String senderEmail;

    public void sendPurchaseConfirmation(Purchase purchase, PurchaseRequest purchaseRequest)
        throws PurchaseConfirmationSendingException {

        try {
            mailSender.send(createPurchaseConfirmationMessage(purchase, purchaseRequest));
        } catch (MailException me) {
            throw new PurchaseConfirmationSendingException("Could not send confirmation email for purchase #" +
                purchase.getId(), me);
        }
    }

    /**
     * Sets the mail sender that will be used to send the confirmation emails.
     * @param mailSender The mail sender that will be used to send the confirmation emails.
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sets the email that will apear in the "from" field of the email messages.
     * @param senderEmail The email that will apear in the "from" field of the email messages.
     */
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * Creates the email confirmation message for the given purchase. This method can be
     * overriden to apply more sofisticated message creation (using Velocity templates for example)
     * @param purchase The purchase to confirm.
     * @param request The purchase request.
     * @return The email confirmation message for the given purchase.
     */
    protected SimpleMailMessage createPurchaseConfirmationMessage(Purchase purchase, PurchaseRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(purchase.getEmail());
        message.setFrom(senderEmail);
        message.setSubject("Purchase Confirmation " + purchase.getPaymentAuthorizationCode());
        message.setText(createMessageText(purchase, request));
        return message;
    }

    protected String createMessageText(Purchase purchase, PurchaseRequest request) {
        Performance performance = request.getPerformance();

        StringBuffer text = new StringBuffer();
        text.append("Your purchase of seats for the performance ").append(performance.getShow().getName());
        text.append(" on ").append(performance.getDateAndTime()).append(" has been accepted.\n\n");
        text.append("Your credit card No. ").append(purchase.getEncryptedCardNumber()).append(" will be debited $");
        text.append(request.getReservation().getBooking().getPrice()).append(".\n\n");
        text.append("You have been allocated seats ");
        Seat[] seats = request.getReservation().getSeats();
        for (int i=0; i<seats.length; i++) {
            if (i > 0) {
                text.append("; ");
            }
            text.append(seats[i].getName());
        }
        text.append(".\n\n");
        if (purchase.isUserCollected()) {
            text.append("Your ticket are waiting for you in the ticketing stand at the theatre");
        } else {
            text.append("Your tickets will be sent by post to the following address:\n\n");
            Address deliveryAddress = getDeliveryAddress(purchase);
            text.append(purchase.getCustomerName()).append("\n");
            text.append(deliveryAddress.getStreet()).append("\n");
            text.append(deliveryAddress.getCity()).append("\n");
            text.append(deliveryAddress.getCountry()).append("\n");
            text.append(deliveryAddress.getPostcode());
        }
        text.append("\n\n");
        text.append("Your booking reference is ").append(purchase.getPaymentAuthorizationCode()).append(".");
        text.append("Please retain it for your records (you can print this mail). You will need to have ");
        text.append("this number ready in case of enquiry.\n\n");
        return text.toString();
    }

    private Address getDeliveryAddress(Purchase purchase) {
        if (purchase.getDeliveryAddress() == null) {
            return purchase.getBillingAddress();
        }
        Address deliveryAddress = purchase.getDeliveryAddress();
        if (deliveryAddress.getCountry() == null || deliveryAddress.getCountry().length() == 0) {
            return purchase.getBillingAddress();
        }
        return deliveryAddress;
    }
}
