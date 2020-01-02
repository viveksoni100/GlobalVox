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
 * Represents the module that handles credit card payments.
 */
public interface PaymentProcessor {

    /**
     * Validates the given purhcase request details for the payment.
     * @param purchaseRequest The purchase request.
     * @throws CreditCardValidationException Thrown when the credit card details of the given request
     *         are not valid.
     */
    public void validate(PurchaseRequest purchaseRequest) throws CreditCardValidationException;

    /**
     * Processes the given payment using an external payment system.
     * @param purchaseRequest The details of the payment to process.
     * @return A payment confirmation code.
     */
    public String process(PurchaseRequest purchaseRequest) throws PaymentException;

}
