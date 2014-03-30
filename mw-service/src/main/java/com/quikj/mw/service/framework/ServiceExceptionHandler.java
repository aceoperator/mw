/**
 * 
 */
package com.quikj.mw.service.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author amit
 * 
 */
public class ServiceExceptionHandler implements HandlerExceptionResolver {

	protected final Log logger = LogFactory.getLog(getClass());

	private int order;

	public ServiceExceptionHandler() {
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest req,
			HttpServletResponse rsp, Object handler, Exception e) {

		String msg = getRootExceptionMessage(e);
		String session = req.getSession(true).getId();
		String user = req.getUserPrincipal() == null ? "(not logged in)" : req.getUserPrincipal().getName();
		logger.error(
				"Exception occured for user "
						+ user
						+ ", session: "
						+ session
						+ " ("
						+ req.getRemoteAddr()
						+ "). Exception: "
						+ e.getClass().getName()
						+ " thrown by object "
						+ (handler == null ? "NULL" : handler
								+ ". The error message is - " + msg), e);

		com.quikj.mw.core.value.Error error = new com.quikj.mw.core.value.Error();
		error.setAdditionalInformation(msg);
		error.setSessionCode(session);
		ModelAndView mav = new ModelAndView("exceptionView");
		mav.addObject(error);
		return mav;
	}

	private String getRootExceptionMessage(Throwable e) {
		if (e.getCause() != null) {
			return getRootExceptionMessage(e.getCause());
		} else {
			return e.getMessage();
		}
	}
}
