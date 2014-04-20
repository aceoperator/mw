/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author amit
 *
 */
public interface CaptchaService {

	void getCaptcha(HttpServletRequest request, HttpServletResponse response, String size);

}
