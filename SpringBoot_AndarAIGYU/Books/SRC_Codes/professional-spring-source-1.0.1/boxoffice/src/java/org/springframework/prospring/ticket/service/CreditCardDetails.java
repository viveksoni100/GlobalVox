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

import java.util.*;

/**
 * Holds the details of a credit card.
 */
public class CreditCardDetails {

    private final static String ENCRYPTED_PREFIX = "XXXX-XXXX-XXXX-";

    // the type of the credit card (visa, master card, etc...)
    private String type;

    // the name of the card owner as shown on the card.
    private String nameOnCard;

    // the number of the credit card.
    private String cardNumber;

    // the expiry date of the credit card.
    private Date expiryDate;

    /**
     * Empty constructor to support javabean spec.
     */
    public CreditCardDetails() {
    }

    /**
     * Constructs a new credit card datails.
     * @param type The type of the card.
     * @param nameOnCard The name of the card owner.
     * @param cardNumber The card number.
     * @param expiryDate The expiry date of the card.
     */
    public CreditCardDetails(String type, String nameOnCard, String cardNumber, Date expiryDate) {
        this.type = type;
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the type of the credit card.
     * @return The type of the credit card.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of this credit card.
     * @param type The type of this credit card.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the name of the credit card owner as shown on the card.
     * @return The name of the credit card owner as shown on the card.
     */
    public String getNameOnCard() {
        return nameOnCard;
    }

    /**
     * Sets the name of the credit card owner as shown on the card.
     * @param nameOnCard The name of the credit card owner as shown on the card.
     */
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    /**
     * Returns the credit card number.
     * @return The credit card number.
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Returns an encrypted version of the credit card number (that can be displayed on site)
     * @return An encrypted version of the credit card number (that can be displayed on site)
     */
    public String getEncryptedCardNumber() {
        String last4Numbers = cardNumber.substring(cardNumber.length()-4);
        return ENCRYPTED_PREFIX + last4Numbers;
    }

    /**
     * Sets the credit card number.
     * @param cardNumber The credit card number.
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Returns the expiry date of the credit card.
     * @return The expiry date of the credit card.
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date of the credit card.
     * @param expiryDate The expiry date of the credit card.
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

}
