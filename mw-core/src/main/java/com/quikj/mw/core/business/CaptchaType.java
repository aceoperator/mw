/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business;

public enum CaptchaType {
	SMALL("small"), LARGE("large");

	String value;

	CaptchaType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static CaptchaType fromValue(String value) {
		for (CaptchaType v : values()) {
			if (v.value().equals(value)) {
				return v;
			}
		}

		return null;
	}
}