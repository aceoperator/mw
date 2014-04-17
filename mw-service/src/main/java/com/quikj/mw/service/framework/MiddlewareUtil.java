/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.framework;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import com.quikj.mw.core.MiddlewareSecurityException;

/**
 * @author amit
 * 
 */
public class MiddlewareUtil {

	public static final String USERNAME_DELIMITER = ",";

	public static String getUserId() {
		User user = getUser();

		String[] tokens = user.getUsername().split(USERNAME_DELIMITER);
		return tokens[0];
	}

	public static String getDomain() {
		User user = getUser();
		
		String[] tokens = user.getUsername().split(USERNAME_DELIMITER);
		if (tokens.length > 1) {
			return tokens[1];
		}

		return null;
	}

	private static User getUser() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			throw new MiddlewareSecurityException("Not logged in");
		}
		User user = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		return user;
	}
}
