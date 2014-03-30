/**
 * 
 */
package com.quikj.mw.core.business;

import java.io.File;
import java.util.Map;

/**
 * @author Amit Chatterjee
 * 
 */
public interface MailBean {

	void sendMessage(String[] to, String[] cc, String[] bcc,
			String from, String subject, String body, boolean html,
			File[] attachments, File[] inlineFiles,
			Map<String, Object> properties);
}
