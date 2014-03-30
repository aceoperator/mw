/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.quikj.mw.service.MiddlewareServiceException;

/**
 * @author amit
 *
 */
public class BridgeDispatcher {

	private Map<String, CommandExecutor> executors = new HashMap<>();
	
	public void setExecutors(Map<String, CommandExecutor> executors) {
		this.executors = executors;
	}

	public Properties dispatch(String command, Properties properties) {
		CommandExecutor executor = executors.get(command);
		if (executor == null) {
			throw new MiddlewareServiceException("Executor not found");
		}
		
		return executor.execute(properties);
	}
}
