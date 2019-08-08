package com.cmwa.ecc.business.exception;

import org.springframework.mail.MailSendException;

public class MyMailSendException extends MailSendException {
	
	private static final long	serialVersionUID	= 1L;
	
	
	public MyMailSendException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
