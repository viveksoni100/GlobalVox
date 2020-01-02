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

package org.springframework.prospring.ticket.domain;

/**
 * Represents a registered user in the boxoffice system.
 */
public class RegisteredUser {

    // the id of this user.
    private long id;

    // the email of this user.
    private String email;

    // the password of this user.
    private String password;

    // the billing address of this user.
    private Address billingAddress;

    // the delivery address of this user.
    private Address deliveryAddress;

    /**
     * Empty constructor to support javabean spec.
     */
    public RegisteredUser() {
    }

    /**
     * Constructs a new registered user with given email, password, and billing address. The default
     * delivery address will be the same as the billing address.
     * @param email The email of this user.
     * @param password The password of this user.
     * @param billingAddress The billing address (and delivery address) of this user.
     */
    public RegisteredUser(String email, String password, Address billingAddress) {
        this(email, password, billingAddress, billingAddress);
    }

    /**
     * Constructs a new registered user with given email, password, billing address, and delivery address.
     * @param email The email of this user.
     * @param password The password of this user.
     * @param billingAddress The billing address of this user.
     * @param deliveryAddress The delivery address of this user.
     */
    public RegisteredUser(String email, String password, Address billingAddress, Address deliveryAddress) {
        this.email = email;
        this.password = password;
        this.billingAddress = billingAddress;
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Returns the id of this user.
     * @return The id of this user.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this user.
     * @param id The id of this user.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the email of this user.
     * @return The email of this user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of this user.
     * @param email The email of this user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of this user.
     * @return The password of this user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this user.
     * @param password The password of this user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the billing address of this user.
     * @return The billing address of this user.
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the billing address of this user.
     * @param billingAddress The billing address of this user.
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Returns the delivery address of this user.
     * @return The delivery address of this user.
     */
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the delivery address of this user.
     * @param deliveryAddress The delivery address of this user.
     */
    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

}
