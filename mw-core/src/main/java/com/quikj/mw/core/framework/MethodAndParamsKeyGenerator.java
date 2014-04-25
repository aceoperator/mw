/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.framework;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

/**
 * @author amit
 * 
 */
public class MethodAndParamsKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method, Object... args) {
		StringBuilder builder = new StringBuilder();

		builder.append(method.getDeclaringClass().getSimpleName()).append('.')
				.append(method.getName()).append('(');

		for (int i = 0; i < args.length; i++) {
			if (i > 0) {
				builder.append(',');
			}

			builder.append(args[i]);
		}

		builder.append(")");

		return builder.toString();
	}
}
