package com.quikj.mw.core.business.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.springframework.jdbc.datasource.DataSourceUtils;

import com.quikj.mw.core.MiddlewareCoreException;
import com.quikj.mw.core.business.ReportGeneratorBean;

/**
 * @author amit
 * 
 */
public class ReportGeneratorBeanJasperImpl implements ReportGeneratorBean {

	private DataSource dataSource;

	public ReportGeneratorBeanJasperImpl() {
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Map<String, Object> generateReport(Map<String, Object> properties) {
		Connection c = DataSourceUtils.getConnection(dataSource);
		try {
			boolean external = false;
			String reportLocParam = (String) properties
					.get(PROPERTY_REPORT_LOCATION);
			if (reportLocParam != null && reportLocParam.equals("external")) {
				external = true;
			}

			String reportNameParam = (String) properties
					.get(PROPERTY_REPORT_NAME);
			if (reportNameParam == null) {
				throw new MiddlewareCoreException(
						"The report location has not been specified");
			}

			String reportOutputParam = (String) properties
					.get(PROPERTY_OUTPUT_LOCATION);
			if (reportOutputParam == null) {
				reportNameParam = new File(System.getProperty("user.home")
						+ "/.mw/reports").getAbsolutePath();
			}

			File reportOutputDir = new File(reportOutputParam);
			if (!reportOutputDir.isAbsolute()) {
				reportOutputDir = new File(System.getProperty("user.home")
						+ "/.mw/reports", reportOutputParam);
			}

			InputStream reportStream = null;
			if (external) {
				String reportPath = System.getProperty("user.home")
						+ "/.mw/conf/reports/" + reportNameParam + ".jrxml";
				reportStream = new FileInputStream(reportPath);
			} else {
				String reportPath = "/reports/" + reportNameParam + ".jrxml";
				reportStream = getClass().getResourceAsStream(reportPath);
				if (reportStream == null) {
					throw new MiddlewareCoreException(
							"The report defintion file could not be opened");
				}
			}

			String reportFormat = (String) properties
					.get(PROPERTY_OUTPUT_FORMAT);
			if (reportFormat == null) {
				reportFormat = DEFAULT_FORMAT;
			}

			Date time = new Date();
			File f = null;
			if (reportFormat.equals(FORMAT_HTML)) {
				File d = new File(reportOutputDir, "/report_" + reportNameParam
						+ Long.toString(time.getTime()));
				d.mkdir();
				f = new File(d.getAbsolutePath(), "index.html");
			} else {
				f = new File(reportOutputDir.getAbsolutePath(), reportNameParam
						+ "_" + Long.toString(time.getTime()) + "."
						+ reportFormat);
			}

			String outputFileName = f.getAbsolutePath();

			JasperReport report = JasperCompileManager
					.compileReport(reportStream);
			reportStream.close();

			JasperPrint print = JasperFillManager.fillReport(report,
					properties, c);

			if (reportFormat.equals(FORMAT_XML)) {
				JasperExportManager.exportReportToXmlFile(print,
						outputFileName, true);
			} else if (reportFormat.equals(FORMAT_HTML)) {
				JasperExportManager.exportReportToHtmlFile(print,
						outputFileName);
			} else if (reportFormat.equals(FORMAT_PDF)) {
				JasperExportManager
						.exportReportToPdfFile(print, outputFileName);
			} else if (reportFormat.equals(FORMAT_XLS)) {
				FileOutputStream os = new FileOutputStream(outputFileName);
				JRXlsExporter export = new JRXlsExporter();
				export.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
				export.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, os);
				export.exportReport();
				os.close();
			} else {
				throw new MiddlewareCoreException(
						"The output format is not supported");
			}

			// Output parameters
			properties.put(PROPERTY_REPORT_GEN_TIME, time);

			if (reportFormat.equals(FORMAT_HTML)) {
				properties.put(PROPERTY_OUTPUT_NAME, f.getParent());
			} else {
				properties.put(PROPERTY_OUTPUT_NAME, outputFileName);
			}

		} catch (Exception e) {
			throw new MiddlewareCoreException(e);
		} finally {
			DataSourceUtils.releaseConnection(c, dataSource);
		}

		return properties;
	}
}
