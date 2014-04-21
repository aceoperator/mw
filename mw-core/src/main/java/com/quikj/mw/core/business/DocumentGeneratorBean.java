/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business;

import java.util.Map;

/**
 * @author amit
 * 
 */
public interface DocumentGeneratorBean {

	void generate(Map<String, Object> properties, String outputRoot,
			String templateRoot, String rootDirName, String[] excludePatterns);
}
