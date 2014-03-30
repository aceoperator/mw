/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business.bean;

import groovy.lang.GroovyShell;

import java.io.File;

import com.quikj.mw.core.MiddlewareCoreException;
import com.quikj.mw.core.business.ScriptingBean;

/**
 * @author amit
 *
 */
public class ScriptingBeanGroovyImpl implements ScriptingBean {

	private GroovyShell groovyShell;
	
	public ScriptingBeanGroovyImpl() {
	}
	
	public GroovyShell getGroovyShell() {
		return groovyShell;
	}

	public void setGroovyShell(GroovyShell groovyShell) {
		this.groovyShell = groovyShell;
	}

	@Override
	public Object evaluate(String scriptFile) {
		File file = getScriptFile(scriptFile);
		
		try {
			return groovyShell.evaluate(file);
		} catch (Exception e) {
			throw new MiddlewareCoreException(e);
		}
	}
	
	@Override
	public Object run(String scriptFile, String[] args) {
		File file = getScriptFile(scriptFile);
		
		try {
			return groovyShell.run(file, args);
		} catch (Exception e) {
			throw new MiddlewareCoreException(e);
		}
	}

	private File getScriptFile(String scriptFile) {
		File file = new File(scriptFile);
		if (!file.isAbsolute()) {		
			file = new File(System.getProperty("user.home") + "/.mw/groovy", scriptFile);
		}
		return file;
	}
}
