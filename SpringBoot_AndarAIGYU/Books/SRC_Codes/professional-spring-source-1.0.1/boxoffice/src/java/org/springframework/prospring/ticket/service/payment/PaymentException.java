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
 * Exception that is thrown when a payment process failed.
 *
 * @see PaymentProcessor#process(org.springframework.prospring.ticket.service.PurchaseRequest)
 */
public class PaymentException extends BaseApplicationException {

    public PaymentException(String errorCode) {
        super(errorCode);
    }

    public PaymentException(String message, String errorCode) {
        super(errorCode, message);
    }

    public PaymentException(Throwable cause, String errorCode) {
        super(errorCode, cause);
    }

    public PaymentException(String message, Throwable cause, String errorCode) {
        super(errorCode, message, cause);
    }

}
