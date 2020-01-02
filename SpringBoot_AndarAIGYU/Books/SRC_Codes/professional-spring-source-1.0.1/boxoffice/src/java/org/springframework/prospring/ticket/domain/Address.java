package org.springframework.prospring.ticket.domain;

import org.springframework.util.*;

/**
 * Represents an address in the system.
 */
public class Address {

    // the country of this address.
    private String country;

    // the city of this address.
    private String city;

    // the street of this address.
    private String street;

    // the postcode of this address.
    private String postcode;

    /**
     * Empty constructor to support javabean spec.
     */
    public Address() {
    }

    /**
     * Constructs a new address.
     * @param country The country of the address.
     * @param city The city of the address.
     * @param street The street of the address.
     * @param postcode The postcode of the address.
     */
    public Address(String country, String city, String street, String postcode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.postcode = postcode;
    }

    /**
     * Returns the country of this address.
     * @return The country of this address.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of this address.
     * @param country The country of this address.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the city of this address.
     * @return The city of this address.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of this address.
     * @param city The city of this address.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the street of this address.
     * @return The street of this address.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street of this address.
     * @param street The street of this address.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the postcode of this address.
     * @return The postcode of this address.
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Sets the postcode of this address.
     * @param postcode The postcode of this address.
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * @see Object#equals(Object)
     */
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Address)) {
            return false;
        }

        Address address = (Address)o;

        return ObjectUtils.nullSafeEquals(city, address.city) &&
            ObjectUtils.nullSafeEquals(country, address.country) &&
            ObjectUtils.nullSafeEquals(postcode, address.postcode) &&
            ObjectUtils.nullSafeEquals(street, address.street);
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = (country != null) ? country.hashCode() : 0;
        result = 31 * result + ((city != null) ? city.hashCode() : 0);
        result = 31 * result + ((street != null) ? street.hashCode() : 0);
        result = 31 * result + ((postcode != null) ? postcode.hashCode() : 0);
        return result;
    }
}
