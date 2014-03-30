/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service;

/**
 * @author amit
 *
 */
public class MiddlewareServiceException extends RuntimeException {
	
	private static final long serialVersionUID = -3335003686232984431L;

	public MiddlewareServiceException() {
	}

	public MiddlewareServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MiddlewareServiceException(String message) {
		super(message);
	}

	public MiddlewareServiceException(Throwable cause) {
		super(cause);
	}
}
