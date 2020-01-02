package org.springframework.prospring.ticket.web;

import java.util.regex.*;
import java.util.*;

import org.springframework.validation.*;
import org.springframework.prospring.ticket.service.*;
import org.springframework.prospring.ticket.domain.*;

/**
 * A vaildator for the PurchaseRequest.
 */
public class PurchaseRequestValidator implements Validator {

    // the regular expression that matches a credit card number
    private final static String CREDIT_CARD_NUMBER_REG_EXP = "\\d{4}\\-\\d{4}\\-\\d{4}\\-\\d{4}";

    // the pattern used when validating credit card numbers.
    private final static Pattern CREDIT_CARD_NUMBER_PATTERN = Pattern.compile(CREDIT_CARD_NUMBER_REG_EXP);

    // a regular expression that matches an email address.
    private final static String EMAIL_REG_EXP = "^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$";

    // the pattern used when validating emails.
    private final static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REG_EXP);

    /**
     * Return whether or not this object can validate objects
     * of the given class.
     */
    public boolean supports(Class clazz) {
        return PurchaseRequest.class.isAssignableFrom(clazz);
    }

    /**
     * @see Validator#validate(Object, org.springframework.validation.Errors)
     */
    public void validate(Object obj, Errors errors) {

        PurchaseRequest request = (PurchaseRequest)obj;

        // validating the credit card details
        validateCreditCardDetails(errors, request.getCreditCardDetails());

        // validating the billing address
        validateBillingAddress(errors);

        // validating the delivery address
        validateDeliveryAddress(errors, request.getDeliveryAddress());

        // validating the email
        validateEmail(errors, request.getEmail());
    }

    private void validateCreditCardDetails(Errors errors, CreditCardDetails cardDetails) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardDetails.type", "validation.purchaseRequest.creditCardType.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardDetails.nameOnCard", "validation.purchaseRequest.nameOnCard.required");
        validateCreditCardNumber(errors, "creditCardDetails.cardNumber", cardDetails.getCardNumber());
        validateExpiryDate(errors, "creditCardDetails.expiryDate", cardDetails.getExpiryDate());
    }

    private void validateCreditCardNumber(Errors errors, String propertyName, String cardNumber) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, propertyName, "validation.purchaseRequest.creditCardNumber.required");

        if (cardNumber == null || cardNumber.length() == 0) {
            return;
        }

        if (!CREDIT_CARD_NUMBER_PATTERN.matcher(cardNumber).matches()) {
            errors.rejectValue(propertyName, "validation.purchaseRequest.creditCardNumber.format");
        }
    }

    private void validateExpiryDate(Errors errors, String propertyName, Date expiryDate) {
        ValidationUtils.rejectIfEmpty(errors, propertyName, "validation.purchaseRequest.creditCardExpiryDate.required");

        if (expiryDate == null) {
            return;
        }

        if (expiryDate.before(new Date())) {
            errors.rejectValue(propertyName, "validation.purchaseRequest.creditCardExpiryDate.expired");
        }
    }

    private void validateBillingAddress(Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.country", "validation.purchaseRequest.billingAddressCountry.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.city", "validation.purchaseRequest.billingAddressCity.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.street", "validation.purchaseRequest.billingAddressStreet.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.postcode", "validation.purchaseRequest.billingAddressPostcode.required");
    }

    private void validateDeliveryAddress(Errors errors, Address deliveryAddress) {
        if (deliveryAddress == null || deliveryAddress.getCountry() == null || deliveryAddress.getCountry().length() == 0) {
            return;
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveryAddress.city", "validation.purchaseRequest.deliveryAddressCity.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveryAddress.street", "validation.purchaseRequest.deliveryAddressStreet.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveryAddress.postcode", "validation.purchaseRequest.deliveryAddressPostcode.required");
    }

    private void validateEmail(Errors errors, String email) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "validation.purchaseRequest.email.required");

        if (email == null || email.length() == 0) {
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.rejectValue("email", "validation.purchaseRequest.email.format");
        }

    }

}
