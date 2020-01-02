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

package org.springframework.prospring.ticket.service;

import org.springframework.prospring.ticket.domain.*;

/**
 * Represents a request of tickets purchase for a performance.
 */
public class PurchaseRequest {

    // the reservation associated with this request.
    private Reservation reservation;

    // the performance associated with this request.
    private Performance performance;

    // the credit card details for the purchase.
    private CreditCardDetails creditCardDetails;

    // the billing address for the purchase.
    private Address billingAddress;

    // the delivery address for this purchase.
    private Address deliveryAddress;

    // the email of the customer who makes this purchase.
    private String email;

    // indicates whether the customer will collect the tickets him/herself.
    private boolean collect;

    /**
     * Constructs a purchase request.
     */
    public PurchaseRequest() {
    }

    /**
     * Returns the reservation associated with this purchase request.
     * @return The reservation associated with this purchase request.
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * Sets the reservation associated with this purchase request.
     * @param reservation The reservation associated with this purchase request.
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * Returns the performance associated with this request.
     * @return The performance associated with this request.
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * Sets the performance associated with this request.
     * @param performance The performance associated with this request.
     */
    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    /**
     * Returns the credit card details of this purchase request.
     * @return The credit card details of this purchase request.
     */
    public CreditCardDetails getCreditCardDetails() {
        return creditCardDetails;
    }

    /**
     * Sets the credit card details of this purchase request.
     * @param creditCardDetails The credit card details of this purchase reqeust.
     */
    public void setCreditCardDetails(CreditCardDetails creditCardDetails) {
        this.creditCardDetails = creditCardDetails;
    }

    /**
     * Returns the billing address of this purchase request.
     * @return The billing address of this purchase request.
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the billing address of this purchase request.
     * @param billingAddress The billing address of this purchase request.
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Returns the delivery address of this purchase request.
     * @return The delivery address of this purchase request, null if the delivery address is the same
     * as the billing address.
     */
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the delivery address of this purchase request.
     * @param deliveryAddress The delivery address of this purchase request.
     */
    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Returns the email of the customer who performs the purchase.
     * @return The email of the customer who performs the purchase.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the customer who performs the purchase.
     * @param email The email of the customer who performs the purchase.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns whether the customer will collect the tickets him/herself or they should be sent by post.
     * @return true if the customer will collect the tickets, false if they should be sent by post.
     */
    public boolean isCollect() {
        return collect;
    }

    /**
     * Sets whether the customer will collect the tickets him/herself or they should be sent by post.
     * @param collect Indicates whether the customer will collect the tickets him/herself or they should be sent by post.
     */
    public void setCollect(boolean collect) {
        this.collect = collect;
    }

}
