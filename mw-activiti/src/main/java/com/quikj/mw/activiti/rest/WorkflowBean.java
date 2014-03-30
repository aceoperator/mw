package com.quikj.mw.activiti.rest;

import java.util.Map;

public interface WorkflowBean {

	public String startProcessInstanceById(String processDefinitionKey,
			String businessKey, Map<String, Object> variables);

	public void signal(String processInstanceId, String activityId,
			Map<String, Object> variables);

}