/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.activiti.rest.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.quikj.mw.activiti.bridge.MiddlewareActivitiException;
import com.quikj.mw.activiti.rest.WorkflowBean;

/**
 * @author amit
 * 
 */
public class WorkflowBeanImpl extends RestTemplate implements WorkflowBean {

	private String workflowEngineUrl;
	private String workflowEngineUser;
	private String workflowEnginePassword;

	public void setWorkflowEngineUrl(String workflowEngineUrl) {
		this.workflowEngineUrl = workflowEngineUrl;
	}

	public void setWorkflowEngineUser(String workflowEngineUser) {
		this.workflowEngineUser = workflowEngineUser;
	}

	public void setWorkflowEnginePassword(String workflowEnginePassword) {
		this.workflowEnginePassword = workflowEnginePassword;
	}

	@Override
	public String startProcessInstanceById(String processDefinitionKey,
			String businessKey, Map<String, Object> variables) {

		JSONObject body = new JSONObject();
		body.put("processDefinitionKey", processDefinitionKey);

		if (businessKey != null) {
			body.put("businessKey", businessKey);
		}

		if (variables != null) {
			for (Entry<String, Object> var : variables.entrySet()) {
				body.put(var.getKey(), (String) var.getValue());
			}
		}

		HttpHeaders headers = createHeaders();

		ResponseEntity<String> response = postForEntity(workflowEngineUrl
				+ "/service/process-instance",
				new HttpEntity<String>(body.toString(), headers), String.class);

		HttpStatus status = response.getStatusCode();
		if (status != HttpStatus.OK) {
			throw new MiddlewareActivitiException(
					"Request to the workflow engine returned unexpected status "
							+ status);
		}

		JSONObject parsedResponse = new JSONObject(response.getBody());
		return parsedResponse.getString("processInstanceId");
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(mediaTypes);

		String auth = workflowEngineUser + ":" + workflowEnginePassword;
		String encoding = new String(new Base64().encode(auth.getBytes()));
		encoding = encoding.replaceAll("\n", "");
		String authHeaderValue = "BASIC " + encoding;
		headers.add(org.apache.http.HttpHeaders.AUTHORIZATION, authHeaderValue);
		return headers;
	}

	@Override
	public void signal(String processInstanceId, String activityId,
			Map<String, Object> variables) {
		JSONObject body = new JSONObject();
		body.put("activityId", activityId);

		if (variables != null) {
			for (Entry<String, Object> var : variables.entrySet()) {
				body.put(var.getKey(), (String) var.getValue());
			}
		}

		HttpHeaders headers = createHeaders();

		ResponseEntity<String> response = postForEntity(workflowEngineUrl
				+ "/service/process-instance/{processInstanceId}/signal",
				new HttpEntity<String>(body.toString(), headers), String.class,
				processInstanceId);

		HttpStatus status = response.getStatusCode();
		if (status != HttpStatus.OK) {
			throw new MiddlewareActivitiException(
					"Request to the workflow engine returned unexpected status "
							+ status);
		}

		JSONObject parsedResponse = new JSONObject(response.getBody());
		boolean statusText = parsedResponse.getBoolean("success");
		if (!statusText) {
			throw new MiddlewareActivitiException(
					"Request to the workflow engine returned unexpected status text "
							+ statusText);
		}
	}
}
