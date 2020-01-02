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

package org.springframework.prospring.ticket.service.payment;

import org.springframework.prospring.ticket.service.*;

/**
 * Exception that is thrown when a credit card is not valid.
 *
 * @see PaymentProcessor#validate(org.springframework.prospring.ticket.service.PurchaseRequest)
 */
public class CreditCardValidationException extends BaseApplicationException {

    // the error code.
    private final static String ERROR_CODE = "payment.creditcard.invalid";

    public CreditCardValidationException(Throwable ex) {
        super(ERROR_CODE, ex);
    }

    public CreditCardValidationException(String message) {
        super(ERROR_CODE, message);
    }

}
