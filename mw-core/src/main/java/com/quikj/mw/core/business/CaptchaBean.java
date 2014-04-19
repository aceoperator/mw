/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business;

import java.util.Locale;

/**
 * @author amit
 *
 */
public interface CaptchaBean {

	byte[] getCaptchaPngImage(String captchaId, Locale locale);

	void validateCaptcha(String captchaId, String captcha);
}
