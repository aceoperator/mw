/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.framework;

import java.util.Properties;

/**
 * @author amit
 * 
 */
public class MiddlewareGlobalProperties {

	public static final String VALIDATE_CAPTCHA_ON_GET_SEC_QUESTIONS = "com.quikj.mw.service.properties.validateCaptchaOnGetSecQuestion";

	private Properties properties = new Properties();

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
