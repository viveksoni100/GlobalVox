package org.springframework.prospring.ticket.web.confirmation;

/**
 * Exception used to indicate a problem sending a purchase confirmation
 */
public class PurchaseConfirmationSendingException extends Exception {

    public PurchaseConfirmationSendingException(String message) {
        super(message);
    }

    public PurchaseConfirmationSendingException(String message, Throwable cause) {
        super(message, cause);
    }

}
