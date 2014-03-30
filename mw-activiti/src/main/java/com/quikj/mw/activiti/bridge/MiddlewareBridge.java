/**
 * 
 */
package com.quikj.mw.activiti.bridge;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.delegate.DelegateExecution;

import com.quikj.mw.activiti.rest.MiddlewareClient;
import com.quikj.mw.activiti.value.ProcessAttributes;
import com.quikj.mw.activiti.value.ProcessProperty;

/**
 * @author amit
 * 
 */
public class MiddlewareBridge {

	private MiddlewareClient mwClient;

	public void setMwClient(MiddlewareClient mwClient) {
		this.mwClient = mwClient;
	}

	public void execute(DelegateExecution execution, String method) {
		ProcessAttributes attributes = new ProcessAttributes();

		attributes.setMethod(method);

		attributes.setProcessId(execution.getProcessDefinitionId());
		attributes.setProcessInstanceId(execution.getProcessInstanceId());
		attributes.setActivityId(execution.getCurrentActivityId());

		addInputProperties(execution.getVariables(), attributes.getInputProperties());

		ProcessAttributes returnAttributes = mwClient.invokeService(attributes);

		addOutputProperties(execution.getVariables(), returnAttributes.getOutputProperties());
	}

	private void addInputProperties(Map<String, Object> variables,
			List<ProcessProperty> processProperties) {
		for (Entry<String, Object> variable : variables.entrySet()) {
			String key = variable.getKey();
			Object value = variable.getValue();
			processProperties.add(new ProcessProperty(key, value.toString()));
		}
	}
	
	private void addOutputProperties(Map<String, Object> variables,
			List<ProcessProperty> processProperties) {		
		for (ProcessProperty property: processProperties) {
			variables.put(property.getKey(), property.getValue());
		}
	}
}
