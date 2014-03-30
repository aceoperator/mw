/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest;

import com.quikj.mw.activiti.value.ProcessAttributes;

/**
 * @author amit
 *
 */
public interface BridgeService {
	ProcessAttributes invokeService(ProcessAttributes attributes);
}
