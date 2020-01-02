package org.springframework.prospring.ticket.domain;

import java.util.*;

/**
 * Represents a purchase in the boxoffice system.
 */
public class Purchase {

    // the id of this purchase.
    private long id;

    // the authorization code for the payment of this purchase.
    private String paymentAuthorizationCode;

    // the date of this purchase.
    private Date purchaseDate;

    // the name of the customer who made this purchase.
    private String customerName;

    // the encrypted credit card number that was used for this purchase.
    private String encryptedCardNumber;

    // the email of the customer who made this puchase.
    private String email;

    // the billing address for this purchase.
    private Address billingAddress;

    // the delivery address for this purchase.
    private Address deliveryAddress;

    // the registered user who made this purchase.
    private RegisteredUser registeredUser;

    // indicates whether the purchased tickets are/was collected by the user
    private boolean userCollected;

    /**
     * Empty constructor to support javabean spec.
     */
    public Purchase() {
    }

    /**
     * Constructs a new purchase.
     * @param paymentAuthorizationCode The authorization code of this purchase.
     * @param purchaseDate The date of this purchase.
     * @param customerName The name of the customer that made the purchase.
     * @param encryptedCardNumber The encrypted credit card number that was used for the purchase.
     * @param email The email of the customer who made the purchase.
     * @param billingAddress The billing address for this purchase.
     * @param deliveryAddress The delivery address for this purchase.
     */
    public Purchase(
        String paymentAuthorizationCode,
        Date purchaseDate,
        String customerName,
        String encryptedCardNumber,
        String email,
        Address billingAddress,
        Address deliveryAddress) {

        this.paymentAuthorizationCode = paymentAuthorizationCode;
        this.purchaseDate = purchaseDate;
        this.customerName = customerName;
        this.encryptedCardNumber = encryptedCardNumber;
        this.email = email;
        this.billingAddress = billingAddress;
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Returns the id of this purchase.
     * @return The id of this purchase.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of this purchase.
     * @param id The id of this purchase.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the authorization code of the payment for this purchase.
     * @return The authorization code of the payment for this purchase.
     */
    public String getPaymentAuthorizationCode() {
        return paymentAuthorizationCode;
    }

    /**
     * Sets the authorization code of the payment for this purchase.
     * @param paymentAuthorizationCode The authorization code of the payment for this purchase.
     */
    public void setPaymentAuthorizationCode(String paymentAuthorizationCode) {
        this.paymentAuthorizationCode = paymentAuthorizationCode;
    }

    /**
     * Returns the date on which this purchase was made.
     * @return The date on which this purchase was made.
     */
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Sets the date on which this purchase was made.
     * @param purchaseDate The date on which this purchase was made.
     */
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Returns the email of the customer who made the purchase.
     * @return The email of the customer who made the purchase.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the customer who made this purchase.
     * @param email The email of the customer who made this purchase.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the billing address for this purchase.
     * @return The billing address for this purchase.
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * Sets the billing addres for this purchase.
     * @param billingAddress The billing addres for this purchase.
     */
    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    /**
     * Returns the delivery address of this purchase.
     * @return The delivery address of this purchase.
     */
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets the delivery address of this purchase.
     * @param deliveryAddress The delivery address of this purchase.
     */
    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Returns the name of the customer who made this purchase.
     * @return The name of the customer who made this purchase.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the name of the customer who made this purchase.
     * @param customerName The name of the customer who made this purchase.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Returns the encrypted credit card number that was used for this purchase.
     * @return The encrypted credit card number that was used for this purchase.
     */
    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    /**
     * Sets the encrypted credit card number that was used for this purchase.
     * @param encryptedCardNumber The encrypted credit card number that was used for this purchase.
     */
    public void setEncryptedCardNumber(String encryptedCardNumber) {
        this.encryptedCardNumber = encryptedCardNumber;
    }

    /**
     * Returns the registered user who made this purchase.
     * @return The registered user who made this purchase or null if the customer who made this purchse did not register.
     */
    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    /**
     * Sets the registered user who made this purchase.
     * @param registeredUser The registered user who made this purchase.
     */
    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    /**
     * Returns whether the purchased tickets are/were collected by the customer.
     * @return True if the tickets are/were collected by the customer who made the purchase.
     */
    public boolean isUserCollected() {
        return userCollected;
    }

    /**
     * Sets whether the purchased tickets are/were collected by the customer.
     * @param userCollected Indicates whether the tickets are/were collected by the customer who made the purchase.
     */
    public void setUserCollected(boolean userCollected) {
        this.userCollected = userCollected;
    }
}
