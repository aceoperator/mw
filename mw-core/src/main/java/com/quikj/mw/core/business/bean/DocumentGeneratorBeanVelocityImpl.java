/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business.bean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.quikj.mw.core.MiddlewareCoreException;
import com.quikj.mw.core.business.DocumentGeneratorBean;

/**
 * @author amit
 * 
 */
public class DocumentGeneratorBeanVelocityImpl implements DocumentGeneratorBean {

	private VelocityEngine velocity;

	public void setVelocity(VelocityEngine velocity) {
		this.velocity = velocity;
	}

	@Override
	public void generate(Map<String, Object> properties, String outputRoot,
			String templateRoot, String rootDirName) {
		File templateRootDir = new File(System.getProperty("user.home")
				+ "/.mw/velocity/" + templateRoot);

		File outputRootDir = new File(outputRoot, rootDirName);

		if (!outputRootDir.mkdir()) {
			throw new MiddlewareCoreException(
					"Output directory could not be created - "
							+ outputRootDir.getAbsolutePath());
		}

		try {
			generateDocuments(templateRootDir, outputRootDir, properties,
					templateRoot);
		} catch (MiddlewareCoreException e) {
			throw e;
		} catch (Exception e) {
			throw new MiddlewareCoreException(e);
		}
	}

	private void generateDocuments(File templateDir, File outputDir,
			Map<String, Object> properties, String templatePath)
			throws VelocityException, IOException {
		File[] files = templateDir.listFiles();
		if (files == null) {
			LogFactory.getLog(getClass()).warn(
					"There are no files to generate in "
							+ templateDir.getAbsolutePath());
			return;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				File childDir = new File(outputDir.getAbsolutePath(),
						file.getName());
				String childTemplatePath = templatePath
						+ (templatePath.isEmpty() ? "" : "/") + file.getName();

				if (!childDir.mkdir()) {
					throw new MiddlewareCoreException(
							"Output directory could not be created - "
									+ childDir.getAbsolutePath());
				}

				generateDocuments(file, childDir, properties, childTemplatePath);
			} else if (file.isFile() && file.getName().endsWith(".vm")) {
				// Create the file from the velocity template
				File outputFile = new File(outputDir, file.getName().substring(
						0, file.getName().length() - 3));
				FileWriter writer = new FileWriter(outputFile);
				VelocityEngineUtils.mergeTemplate(velocity, templatePath + "/"
						+ file.getName(), properties, writer);
				writer.close();
			} else {
				// Copy the rest of the files to the destination
				FileUtils.copyFile(file, outputDir);
			}
		}
	}
}
