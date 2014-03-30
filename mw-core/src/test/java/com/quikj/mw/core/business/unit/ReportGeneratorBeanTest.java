/**
 * 
 */
package com.quikj.mw.core.business.unit;

import static org.junit.Assert.assertTrue;

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

import com.quikj.mw.core.business.ReportGeneratorBean;

/**
 * @author amit
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/META-INF/MwCoreSpringBase.xml",
	"/META-INF/MwCoreSpringBeans.xml", "/TestOverrides.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ReportGeneratorBeanTest {

	@Autowired
	private ReportGeneratorBean reportGeneratorBean;

	public ReportGeneratorBeanTest() {
	}

	public void setReportGeneratorBean(ReportGeneratorBean reportGeneratorBean) {
		this.reportGeneratorBean = reportGeneratorBean;
	}

	public ReportGeneratorBean getReportGeneratorBean() {
		return reportGeneratorBean;
	}

	@Test
	public void testReportGeneratorOperations() throws IOException {
		
		File output = new File(System.getProperty("user.home")
				+ "/.mw/reports/test");
		
		FileUtils.deleteDirectory(output);
		assertTrue(output.mkdir());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ReportGeneratorBean.PROPERTY_REPORT_NAME,
				"test_report");
		params.put(ReportGeneratorBean.PROPERTY_REPORT_LOCATION, "internal");
		params.put(ReportGeneratorBean.PROPERTY_OUTPUT_FORMAT,
				ReportGeneratorBean.FORMAT_XML);
		params.put(ReportGeneratorBean.PROPERTY_OUTPUT_LOCATION, "test");		
		params = reportGeneratorBean.generateReport(params);
		File reportOutput = new File((String) params.get(ReportGeneratorBean.PROPERTY_OUTPUT_NAME));
		assertTrue(reportOutput.exists() && reportOutput.isFile());
		
		FileUtils.deleteDirectory(output);
		assertTrue(output.mkdir());
		
		params.put(ReportGeneratorBean.PROPERTY_OUTPUT_FORMAT,
				ReportGeneratorBean.FORMAT_XLS);
		params = reportGeneratorBean.generateReport(params);
		reportOutput = new File((String) params.get(ReportGeneratorBean.PROPERTY_OUTPUT_NAME));
		assertTrue(reportOutput.exists() && reportOutput.isFile());
		
		FileUtils.deleteDirectory(output);
		assertTrue(output.mkdir());
		
		params.put(ReportGeneratorBean.PROPERTY_OUTPUT_FORMAT,
				ReportGeneratorBean.FORMAT_PDF);
		params = reportGeneratorBean.generateReport(params);
		reportOutput = new File((String) params.get(ReportGeneratorBean.PROPERTY_OUTPUT_NAME));
		assertTrue(reportOutput.exists() && reportOutput.isFile());

		FileUtils.deleteDirectory(output);
		assertTrue(output.mkdir());
		
		params.put(ReportGeneratorBean.PROPERTY_OUTPUT_FORMAT,
				ReportGeneratorBean.FORMAT_HTML);
		params = reportGeneratorBean.generateReport(params);
		reportOutput = new File((String) params.get(ReportGeneratorBean.PROPERTY_OUTPUT_NAME));
		assertTrue(reportOutput.exists() && reportOutput.isDirectory());
	}
}
