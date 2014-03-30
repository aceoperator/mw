/**
 * 
 */
package com.quikj.mw.core.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.quikj.mw.core.ValidationException;

/**
 * @author amit
 * 
 */
public class Validator {

	private static final Pattern EMAIL_PATTERN = Pattern
			.compile(".+@.+\\.[a-z]+");

	public static String validatePassword(String password)
			throws ValidationException {
		if (password == null) {
			throw new ValidationException("The password cannot be empty");
		}

		if (password.length() < 8) {
			throw new ValidationException(
					"The password must be at least 8 characters in length");
		}

		boolean upper = false;
		boolean lower = false;
		boolean number = false;

		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) {
				upper = true;
			} else if (Character.isLowerCase(c)) {
				lower = true;
			} else if (Character.isDigit(c)) {
				number = true;
			}
		}

		if (!upper || !lower || !number) {
			throw new ValidationException(
					"The password must contain at least one upper case, one lower case and one digit");
		}

		return password;
	}

	public static String validateEmail(String email, String fieldName)
			throws ValidationException {
		if (email == null) {
			throw new ValidationException("The " + fieldName
					+ " cannot be empty");
		}

		email = email.trim();

		Matcher m = EMAIL_PATTERN.matcher(email);
		if (!m.matches()) {
			throw new ValidationException("The email format for " + fieldName
					+ " is invalid");
		}

		String[] tokens = email.split("@");
		try {
			InetAddress.getByName(tokens[1]);
		} catch (UnknownHostException e) {
			throw new ValidationException("The email domain for " + fieldName
					+ " is not correct");
		}

		return email;
	}

	public static String validateName(String name, String fieldName)
			throws ValidationException {
		if (name == null || name.trim().length() == 0) {
			throw new ValidationException("The " + fieldName
					+ " cannot be empty");
		}

		name = name.trim();

		char[] nameChar = name.toCharArray();
		for (int i = 0; i < nameChar.length; i++) {
			if (i == 0) {
				if (!Character.isLetter(nameChar[i])) {
					throw new ValidationException("The " + fieldName
							+ " must start with a letter");
				}
			}

			if (Character.isSpaceChar(nameChar[i])) {
				throw new ValidationException("The " + fieldName
						+ " must not have blank spaces");
			}

			if (!Character.isLetter(nameChar[i])
					&& !Character.isDigit(nameChar[i]) && nameChar[i] != '-'
					&& nameChar[i] != '_') {
				throw new ValidationException("The " + fieldName
						+ " must not contain special characters");
			}
		}

		return name;
	}

	public static String validatePhoneNumber(String phoneNumber,
			String fieldName) throws ValidationException {
		if (phoneNumber == null || phoneNumber.trim().length() == 0) {
			throw new ValidationException("The " + fieldName
					+ " cannot be empty");
		}

		phoneNumber = phoneNumber.trim();

		char[] digits = phoneNumber.toCharArray();
		boolean dash = false;
		for (int i = 0; i < digits.length; i++) {
			char digit = digits[i];
			if (digit == '-' && dash) {
				throw new ValidationException("The " + fieldName
						+ " cannot have repeating dash (-) characters");
			} else {
				dash = false;
			}

			if (i == 0) {
				if (digit == '+') {
					continue;
				} else if (digit == '-') {
					throw new ValidationException("The " + fieldName
							+ " cannot start with a dash (-) character");
				}
			}

			if (digit == '-') {
				continue;
			}

			if (Character.isDigit(digit)) {
				continue;
			}

			// All other chases
			throw new ValidationException("The " + fieldName
					+ " cannot have non-numeric characters");
		}

		return phoneNumber;
	}

	public static String validateMandatoryStringField(String value,
			String fieldName) throws ValidationException {

		if (value == null || value.trim().length() == 0) {
			throw new ValidationException("The " + fieldName
					+ " cannot be empty");
		}

		return value.trim();
	}

	public static String validateWebsiteUrl(String url, String fieldName)
			throws ValidationException {
		if (url == null || url.trim().length() == 0) {
			throw new ValidationException("The " + fieldName
					+ " cannot be empty");
		}

		url = url.trim();

		if (!url.toLowerCase().startsWith("http")) {
			url = "http://" + url;
		}

		try {
			URL urlField = new URL(url);

			if (!urlField.getProtocol().equalsIgnoreCase("http")
					&& !urlField.getProtocol().equalsIgnoreCase("https")) {
				throw new ValidationException("The " + fieldName
						+ " must start with a http or https");
			}

			try {
				URLConnection connection = urlField.openConnection();
				connection.getInputStream().close();
			} catch (IOException e) {
				throw new ValidationException(
						"Error occured while trying to connect to site specified by the "
								+ fieldName);
			}
		} catch (MalformedURLException e) {
			throw new ValidationException("The " + fieldName
					+ " does not have valid syntax");
		}

		return url;
	}
}
