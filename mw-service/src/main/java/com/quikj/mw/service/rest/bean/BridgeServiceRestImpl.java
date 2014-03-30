/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest.bean;

import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quikj.mw.activiti.value.ProcessAttributes;
import com.quikj.mw.activiti.value.ProcessProperty;
import com.quikj.mw.service.framework.BridgeDispatcher;
import com.quikj.mw.service.rest.BridgeService;

/**
 * @author amit
 * 
 */
@Controller
@RequestMapping("/bridge")
public class BridgeServiceRestImpl implements BridgeService {

	@Autowired
	private BridgeDispatcher bridgeDispatcher;

	public void setBridgeDispatcher(BridgeDispatcher bridgeDispatcher) {
		this.bridgeDispatcher = bridgeDispatcher;
	}

	@Override
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ProcessAttributes invokeService(
			@RequestBody ProcessAttributes attributes) {
		Properties inputProperties = new Properties();

		for (ProcessProperty property : attributes.getInputProperties()) {
			inputProperties.setProperty(property.getKey(), property.getValue());
		}

		Properties outputProperties = bridgeDispatcher.dispatch(
				attributes.getMethod(), inputProperties);

		for (Entry<Object, Object> entry : outputProperties.entrySet()) {
			attributes.getOutputProperties().add(
					new ProcessProperty((String) entry.getKey(), (String) entry
							.getValue()));
		}

		return attributes;
	}
}
