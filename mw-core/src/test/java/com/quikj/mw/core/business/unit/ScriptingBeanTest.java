/**
 * 
 */
package com.quikj.mw.core.business.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.quikj.mw.core.business.ScriptingBean;

/**
 * @author amit
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/META-INF/MwCoreSpringBase.xml",
	"/META-INF/MwCoreSpringBeans.xml", "/TestOverrides.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ScriptingBeanTest {

	@Autowired
	private ScriptingBean scriptingBean;

	public ScriptingBeanTest() {
	}

	public ScriptingBean getScriptingBean() {
		return scriptingBean;
	}

	@Test
	public void testScriptingOperations() {
		Object result = scriptingBean.evaluate("test_script.groovy");
		assertNotNull(result);
		assertTrue(result instanceof List<?>);
	}
}
