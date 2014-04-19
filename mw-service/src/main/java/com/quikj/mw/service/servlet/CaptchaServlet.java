package com.quikj.mw.service.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octo.captcha.service.CaptchaServiceException;
import com.quikj.mw.core.business.CaptchaBean;
import com.quikj.mw.service.framework.SpringBeanLookup;

public class CaptchaServlet extends HttpServlet {

	private static final long serialVersionUID = 9120211725683240148L;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}

	protected void doGet(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {

		byte[] captchaChallengeAsPng = null;
		try {
			// get the session id that will identify the generated captcha.
			// the same id must be used to validate the response, the session id
			// is a good candidate!
			String captchaId = httpServletRequest.getSession(true).getId();

			CaptchaBean captchaBean = (CaptchaBean) SpringBeanLookup.getBean(
					httpServletRequest.getSession().getServletContext(),
					"captchaBeanLarge");

			String type = httpServletRequest.getParameter("type");
			if (type != null) {
				captchaBean = (CaptchaBean) SpringBeanLookup.getBean(
						httpServletRequest.getSession().getServletContext(),
						"captchaBeanSmall");
			}

			captchaChallengeAsPng = captchaBean.getCaptchaPngImage(captchaId,
					httpServletRequest.getLocale());
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		} catch (CaptchaServiceException e) {
			httpServletResponse
					.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		// flush it in the response
		httpServletResponse.setHeader("Cache-Control", "no-cache");
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setContentType("image/png");
		ServletOutputStream responseOutputStream = httpServletResponse
				.getOutputStream();
		responseOutputStream.write(captchaChallengeAsPng);
		responseOutputStream.flush();
		responseOutputStream.close();
	}
}
