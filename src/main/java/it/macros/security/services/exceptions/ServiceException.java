package it.macros.security.services.exceptions;

import it.macros.security.constants.SecurityMessages;

@SuppressWarnings("serial")
public class ServiceException extends SecurityException
{
	public ServiceException() {
		super();
	}

	/**
	 * @param message
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * @param securityMessages
	 */
	public ServiceException(SecurityMessages securityMessages) {
		super(securityMessages.name());
		this.code = securityMessages.getCode();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}
}