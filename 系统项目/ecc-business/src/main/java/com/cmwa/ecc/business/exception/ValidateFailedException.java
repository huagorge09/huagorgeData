package com.cmwa.ecc.business.exception;

public class ValidateFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidateFailedException() {
		super();
	}

	public ValidateFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateFailedException(String message) {
		super(message);
	}

	public ValidateFailedException(Throwable cause) {
		super(cause);
	}

}
