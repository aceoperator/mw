/**
 * 
 */
package com.quikj.mw.service.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author amit
 * 
 */
public class WebServicesInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();

		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		String ip = request.getRemoteAddr();
		String userName = authentication == null ? "(not logged in)"
				: authentication.getName();
		LogFactory.getLog(getClass()).info(
				"Rest service " + request.getMethod() + " " + requestURI
						+ " invoked by user " + userName + " from IP address "
						+ ip);
		return true;
	}


	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
}
