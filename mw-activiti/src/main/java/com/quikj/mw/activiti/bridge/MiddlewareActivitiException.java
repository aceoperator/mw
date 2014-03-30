/**
 * 
 */
package com.quikj.mw.activiti.bridge;

/**
 * @author amit
 *
 */
public class MiddlewareActivitiException extends RuntimeException {

	private static final long serialVersionUID = 3703726968362477221L;

	public MiddlewareActivitiException() {
		super();
	}

	public MiddlewareActivitiException(String message, Throwable cause) {
		super(message, cause);
	}

	public MiddlewareActivitiException(String message) {
		super(message);
	}

	public MiddlewareActivitiException(Throwable cause) {
		super(cause);
	}
}
