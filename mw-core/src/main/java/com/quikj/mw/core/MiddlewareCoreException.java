/**
 * 
 */
package com.quikj.mw.core;

/**
 * @author amit
 * 
 */
public class MiddlewareCoreException extends RuntimeException {

	private static final long serialVersionUID = 4674616604969804311L;

	public MiddlewareCoreException() {
	}

	public MiddlewareCoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public MiddlewareCoreException(String message) {
		super(message);
	}

	public MiddlewareCoreException(Throwable cause) {
		super(cause);
	}
}
