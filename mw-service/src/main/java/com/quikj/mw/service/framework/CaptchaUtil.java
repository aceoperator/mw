/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.framework;

import javax.servlet.http.HttpServletRequest;

import com.octo.captcha.service.CaptchaServiceException;
import com.quikj.mw.service.MiddlewareServiceException;

/**
 * @author amit
 *
 */
public class CaptchaUtil {

	public static void validateCaptcha(HttpServletRequest request, String captcha) {
		if (captcha == null) {
			throw new MiddlewareServiceException(
					"The captcha text has not been provided");
		}
		
		if (System.getProperty("test.env.proxy") != null) {
			return;
		}
	
		try {
			String captchaId = request.getSession().getId();
			captcha = captcha.toLowerCase();
	
			CaptchaService.CaptchaType ctype = CaptchaService.CaptchaType.LARGE;
			boolean correct = CaptchaService.getInstance(ctype)
					.validateResponseForID(captchaId, captcha);
			if (!correct) {
				throw new MiddlewareServiceException(
						"The captcha text did not match");
			}
		} catch (CaptchaServiceException e) {
			// should not happen, may be thrown if the id is not valid
			throw new MiddlewareServiceException("The captcha text did not match", e);
		}
	}
}
