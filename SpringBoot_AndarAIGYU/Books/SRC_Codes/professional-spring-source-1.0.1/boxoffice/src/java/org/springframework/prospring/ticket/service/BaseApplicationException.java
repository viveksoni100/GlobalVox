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

/**
 * A base class for all application exceptions.
 */
public class BaseApplicationException extends ApplicationException {

    // the error code of this exception.
    private String errorCode;

    /**
     * Constructs a new application exception with the given error code.
     * @param errorCode The error code of the exception.
     */
    public BaseApplicationException(String errorCode) {
        this(errorCode, errorCode);
    }

    /**
     * Constructs a new application exception.
     * @param errorCode The error code of the exception.
     * @param message The message of the exception.
     */
    public BaseApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new application exception.
     * @param errorCode The error code of the exception.
     * @param cause The cause for the exception.
     */
    public BaseApplicationException(String errorCode, Throwable cause) {
        this(errorCode, errorCode, cause);
    }

    /**
     * Constructs a new application exception.
     * @param errorCode The error code of the exception.
     * @param message The message of the exception.
     * @param cause The cause for the exception.
     */
    public BaseApplicationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code of this exception.
     * @return The error code of this exception.
     * @see ApplicationException#getErrorCode()
     */
    public String getErrorCode() {
        return errorCode;
    }

}
