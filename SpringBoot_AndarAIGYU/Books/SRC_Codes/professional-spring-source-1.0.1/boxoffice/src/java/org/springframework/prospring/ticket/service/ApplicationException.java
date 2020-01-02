
package org.springframework.prospring.ticket.service;

import org.springframework.core.*;

/**
 * All application exceptions should subclass this class.
 */
public abstract class ApplicationException extends Exception implements ErrorCoded {

	/**
	 * Constructor for ApplicationException.
	 * @param message The message of the exception
	 */
	public ApplicationException(String message) {
		super(message);
	}

	/**
	 * Constructor for ApplicationException.
	 * @param message The message of the exception.
	 * @param cause The cause of the exception.
	 */
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}


}
