package com.cmwa.ecc.business.exception;

public class CachedException extends Exception {

	private static final long serialVersionUID = 1L;

	public CachedException() {
		super();
	}

	public CachedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CachedException(String message) {
		super(message);
	}

	public CachedException(Throwable cause) {
		super(cause);
	}

}
