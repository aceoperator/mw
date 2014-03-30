/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business;

import java.util.Map;

/**
 * @author amit
 *
 */
public interface ReportGeneratorBean {
	public static final String FORMAT_PDF = "pdf";
	public static final String FORMAT_HTML = "html";
	public static final String FORMAT_XML = "xml";
	public static final String FORMAT_XLS = "xls";
	public static final String DEFAULT_FORMAT = FORMAT_XML;
	
	// Input parameters
	public static final String PROPERTY_OUTPUT_FORMAT = "mw.rpt.outputFormat";
	public static final String PROPERTY_OUTPUT_LOCATION = "mw.rpt.outputLocation";	
	public static final String PROPERTY_REPORT_NAME = "mw.rpt.reportName";
	public static final String PROPERTY_REPORT_LOCATION = "mw.rpt.reportLocation";
	
	// Output parameters
	public static final String PROPERTY_REPORT_GEN_TIME = "mw.rpt.genTime";
	public static final String PROPERTY_OUTPUT_NAME = "mw.rpt.outputName";
	
	Map<String, Object> generateReport(Map<String, Object> properties);
}
