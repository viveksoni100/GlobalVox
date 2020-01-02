package org.springframework.prospring.ticket.service;

import org.springframework.prospring.ticket.domain.*;

/**
 * Indicates an error while trying to reserve or purchase a seat that is no longer
 * available (that is, the seat is already reserved by other customers).
 */
public class RequestedSeatNotAvailableException extends BaseApplicationException {

    private final static String ERROR_CODE = "seatNotAvailable";

    private Performance performance;

    public RequestedSeatNotAvailableException(Performance performance) {
        this("Requested seats for peroformance " + performance.getShow().getName() + " are not available", performance);
    }

    public RequestedSeatNotAvailableException(String message, Performance performance) {
        super(ERROR_CODE, message);
        this.performance = performance;
    }

    /**
     * Returns the performance of the requested seats.
     * @return The performance of the requested seats.
     */
    public Performance getPerformance() {
        return performance;
    }

}
