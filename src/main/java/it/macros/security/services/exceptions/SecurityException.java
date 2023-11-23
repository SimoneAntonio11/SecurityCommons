package it.macros.security.services.exceptions;

import it.macros.security.constants.SecurityMessages;

@SuppressWarnings("serial")
public class SecurityException extends Exception
{
	protected Integer code = null;

	public SecurityException() {
		super();
	}

	/**
	 * @param message
	 */
	public SecurityException(String message) {
		super(message);
	}

	/**
	 * @param securityMessages
	 */
	public SecurityException(SecurityMessages securityMessages) {
		super(securityMessages.name());
		this.code = securityMessages.getCode();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public SecurityException(Throwable cause) {
		super(cause);
	}

	/**
	 * @return Integer
	 */
	public Integer getCode() {
		return code;
	}
}