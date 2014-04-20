/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest.bean;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.octo.captcha.service.CaptchaServiceException;
import com.quikj.mw.core.business.CaptchaBean;
import com.quikj.mw.service.MiddlewareServiceException;
import com.quikj.mw.service.rest.CaptchaService;

/**
 * @author amit
 * 
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaRestServiceImpl implements CaptchaService {
	@Autowired
	private CaptchaBean captchaBeanLarge;

	@Autowired
	private CaptchaBean captchaBeanSmall;

	public void setCaptchaBeanSmall(CaptchaBean captchaBeanSmall) {
		this.captchaBeanSmall = captchaBeanSmall;
	}

	public void setCaptchaBeanLarge(CaptchaBean captchaBeanLarge) {
		this.captchaBeanLarge = captchaBeanLarge;
	}

	@Override
	@RequestMapping(value = "", method = RequestMethod.GET)
	public void getCaptcha(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "size", required = false) String size) {
		try {
			// get the session id that will identify the generated captcha.
			// the same id must be used to validate the response, the session id
			// is a good candidate!
			String captchaId = request.getSession(true).getId();

			CaptchaBean captchaBean = captchaBeanLarge;
			if (size != null && size.equals("small")) {
				captchaBean = captchaBeanSmall;
			}

			byte[] captchaChallengeAsPng = captchaBean.getCaptchaPngImage(
					captchaId, request.getLocale());

			// flush it in the response
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/png");
			ServletOutputStream responseOutputStream = response
					.getOutputStream();
			responseOutputStream.write(captchaChallengeAsPng);
			responseOutputStream.flush();
			responseOutputStream.close();

		} catch (IllegalArgumentException e) {
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e1) {
				LogFactory.getLog(getClass()).error(e1);
			}
			return;
		} catch (CaptchaServiceException e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				LogFactory.getLog(getClass()).error(e1);
			}
			return;
		} catch (Exception e) {
			throw new MiddlewareServiceException(e);
		}
	}
}
