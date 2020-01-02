package org.springframework.prospring.ticket.web.confirmation;

import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.service.*;

/**
 * A strategy for sending a purchase confirmations.
 * This abstracts the form in which the purchase confirmation is sent.
 */
public interface PurchaseConfirmationSender {

    /**
     * Sends a purchase confirmation message.
     * @param purchase The purchase to confirm.
     * @throws PurchaseConfirmationSendingException
     */
    public void sendPurchaseConfirmation(Purchase purchase, PurchaseRequest purchaseRequest)
        throws PurchaseConfirmationSendingException;

}
