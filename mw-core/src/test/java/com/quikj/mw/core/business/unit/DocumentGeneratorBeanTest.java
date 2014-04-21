/**
 * 
 */
package com.quikj.mw.core.business.unit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.quikj.mw.core.business.DocumentGeneratorBean;

/**
 * @author amit
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/META-INF/MwCoreSpringBase.xml",
		"/META-INF/MwCoreSpringBeans.xml", "/TestOverrides.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class DocumentGeneratorBeanTest {

	@Autowired
	private DocumentGeneratorBean documentGeneratorBean;

	public void setDocumentGeneratorBean(
			DocumentGeneratorBean documentGeneratorBean) {
		this.documentGeneratorBean = documentGeneratorBean;
	}

	@Test
	public void testDocGenerationOperations() throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("companyName", "QUIK Computing");

		File root = new File( System.getProperty("user.home")
				+ "/.mw/files/test/www");
		FileUtils.deleteDirectory(root);
		
		documentGeneratorBean.generate(map, System.getProperty("user.home")
				+ "/.mw/files/test", "test/docgen", "www");
	}
}
