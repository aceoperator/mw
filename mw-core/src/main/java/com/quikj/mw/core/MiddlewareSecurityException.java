/**
 * 
 */
package com.quikj.mw.core;

/**
 * @author amit
 * 
 */
public class MiddlewareSecurityException extends RuntimeException {

	private static final long serialVersionUID = 4674616604969804311L;

	public MiddlewareSecurityException() {
	}

	public MiddlewareSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public MiddlewareSecurityException(String message) {
		super(message);
	}

	public MiddlewareSecurityException(Throwable cause) {
		super(cause);
	}
}
