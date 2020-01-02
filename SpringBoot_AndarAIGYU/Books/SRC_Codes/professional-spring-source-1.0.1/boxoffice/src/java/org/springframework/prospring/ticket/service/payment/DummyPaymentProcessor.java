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
 * This is a dummy implmentation of the payment processor interface.
 * It randomly invalidates credit cards and generates random authorization codes
 * for the payments.
 */
public class DummyPaymentProcessor implements PaymentProcessor {

    // characters to generate autorization codes from.
    private final static char[] AUTH_CHARS = {
        'A', 'B', 'C', 'D', 'Y', 'Z', 'Q', 'T', 'J', 'R', 'W', 'V',
        '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    };

    /**
     * Randomly in/validates the given purchase request.
     * @see PaymentProcessor#validate(org.springframework.prospring.ticket.service.PurchaseRequest)
     */
    public void validate(PurchaseRequest purchaseRequest) throws CreditCardValidationException {
        double randomNumber = Math.random() * 1000;
        if (randomNumber < 200) {
            throw new CreditCardValidationException("Credit card is not valid");
        }
    }

    /**
     * Generate a random authorization code for the payment.
     * @see PaymentProcessor#process(org.springframework.prospring.ticket.service.PurchaseRequest)
     */
    public String process(PurchaseRequest purchaseRequest) throws PaymentException {
        StringBuffer code = new StringBuffer();
        for (int i=0; i<10; i++) {
            int charIndex = (int)(Math.random() * AUTH_CHARS.length);
            code.append(AUTH_CHARS[charIndex]);
        }
        return code.toString();
    }

}
