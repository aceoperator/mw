/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business;

/**
 * @author amit
 *
 */
public interface ScriptingBean {

	Object evaluate(String scriptFile);

	Object run(String scriptFile, String[] args);
}
