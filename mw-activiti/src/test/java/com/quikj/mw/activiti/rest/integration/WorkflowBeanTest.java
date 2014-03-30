/**
 * 
 */
package com.quikj.mw.activiti.rest.integration;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.quikj.mw.activiti.rest.WorkflowBean;

/**
 * @author amit
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/TestOverrides.xml" })
public class WorkflowBeanTest {

	@Autowired
	private WorkflowBean workflowBean;

	public void setWorkflowBean(WorkflowBean workflowBean) {
		this.workflowBean = workflowBean;
	}

	@Test
	public void testProcess() throws InterruptedException {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("key1", "value1");
		String processInstanceId = workflowBean.startProcessInstanceById(
				"myFirstProcess", null, variables);
		assertNotNull(processInstanceId);
		
		Thread.sleep(2000L);
		
		variables.clear();
		variables.put("key2", "value2");
		workflowBean.signal(processInstanceId, "someSignal", variables);
	}
}
