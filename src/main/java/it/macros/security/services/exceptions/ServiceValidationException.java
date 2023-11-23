package it.macros.security.services.exceptions;

import it.macros.security.constants.SecurityMessages;

@SuppressWarnings("serial")
public class ServiceValidationException extends SecurityException
{
	public ServiceValidationException() {
		super();
	}

	/**
	 * @param message
	 */
	public ServiceValidationException(String message) {
		super(message);
	}

	/**
	 * @param securityMessages
	 */
	public ServiceValidationException(SecurityMessages securityMessages) {
		super(securityMessages.name());
		this.code = securityMessages.getCode();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ServiceValidationException(Throwable cause) {
		super(cause);
	}
}