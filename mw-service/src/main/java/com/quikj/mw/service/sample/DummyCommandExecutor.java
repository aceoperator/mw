/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.sample;

import java.util.Properties;

import org.apache.commons.logging.LogFactory;

import com.quikj.mw.service.framework.CommandExecutor;

/**
 * @author amit
 *
 */
public class DummyCommandExecutor implements CommandExecutor {

	@Override
	public Properties execute(Properties properties) {
		LogFactory.getLog(getClass()).info("Invoking dummy command executor");
		
		Properties outputProperties = new Properties();
		outputProperties.put("dummyString", "dummy");
		return outputProperties;
	}
}
