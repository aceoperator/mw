/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.sample;

import java.util.Properties;

import com.quikj.mw.service.framework.CommandExecutor;

/**
 * @author amit
 *
 */
public class DummyCommandExecutor implements CommandExecutor {

	@Override
	public Properties execute(Properties properties) {
		properties.put("dummyString", "dummy");
		return properties;
	}

}
