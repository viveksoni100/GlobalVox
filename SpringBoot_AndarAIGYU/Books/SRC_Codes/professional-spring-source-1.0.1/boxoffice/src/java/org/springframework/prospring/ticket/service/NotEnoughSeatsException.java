
package org.springframework.prospring.ticket.service;

/**
 * Indicates an error when requesting to reserve a number of seats while there aren't enough
 * seats to satisfy the request.
 */
public class NotEnoughSeatsException extends BaseApplicationException {

    public final static String ERROR_CODE = "notEnoughSeats";

    private int requested;
	
	private int available;
	
	private long classId;

    /**
     *
     * @param classId The id of the seat class.
     * @param requested The number of requested seats.
     * @param available The number of available seats.
     */
	public NotEnoughSeatsException(long classId, int available, int requested) {
		super(ERROR_CODE, "Requested " + requested + " seats in class " + classId + "; only " + available + " are free");
		this.classId = classId;
		this.requested = requested;
        this.available = available;
    }
	
	public int getRequested() {
		return requested;
	}

    public int getAvailable() {
        return available;
    }

    public long getClassId() {
		return classId;
	}

}
