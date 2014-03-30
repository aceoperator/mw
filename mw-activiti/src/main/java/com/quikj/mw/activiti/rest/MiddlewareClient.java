/**
 * 
 */
package com.quikj.mw.activiti.rest;

import com.quikj.mw.activiti.value.ProcessAttributes;


/**
 * @author amit
 *
 */
public interface MiddlewareClient {

	public ProcessAttributes invokeService(ProcessAttributes attributes);
}
