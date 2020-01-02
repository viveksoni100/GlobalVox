package org.springframework.prospring.ticket.web.confirmation;

import org.springframework.prospring.ticket.domain.*;
import org.springframework.prospring.ticket.service.*;
import org.apache.commons.logging.*;

/**
 * This dummy purchase confirmation sender does nothing but logging the
 * action.
 */
public class DummyMailPurchaseConfirmationSender implements PurchaseConfirmationSender {

    private final static Log logger = LogFactory.getLog(DummyMailPurchaseConfirmationSender.class);

    public void sendPurchaseConfirmation(Purchase purchase, PurchaseRequest purchaseRequest)
        throws PurchaseConfirmationSendingException {

        if (logger.isInfoEnabled()) {
            logger.info("Sending a confirmation email for purchase #" + purchase.getId());
        }
    }
}
