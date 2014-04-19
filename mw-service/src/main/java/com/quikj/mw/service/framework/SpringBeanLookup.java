/**
 * 
 */
package com.quikj.mw.service.framework;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author amit
 * 
 */
public class SpringBeanLookup {
	public static Object getBean(ServletContext context, String beanName) {
		ApplicationContext springContext = WebApplicationContextUtils
				.getWebApplicationContext(context);
		return springContext.getBean(beanName);
	}

	public static <T> T getBean(ServletContext context, Class<T> beanClass) {
		ApplicationContext springContext = WebApplicationContextUtils
				.getWebApplicationContext(context);
		return springContext.getBean(beanClass);
	}
}
