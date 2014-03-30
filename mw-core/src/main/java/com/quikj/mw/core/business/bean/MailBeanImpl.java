/**
 * 
 */
package com.quikj.mw.core.business.bean;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.quikj.mw.core.business.MailBean;

/**
 * @author Amit Chatterjee
 * 
 */
public class MailBeanImpl implements MailBean {

	private static final String TEMPLATE_PREFIX = "template:";

	private JavaMailSender mailSender;

	private String defaultFromEmail;

	private VelocityEngine velocity;

	public MailBeanImpl() {
	}

	public VelocityEngine getVelocity() {
		return velocity;
	}

	public void setVelocity(VelocityEngine velocity) {
		this.velocity = velocity;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getDefaultFromEmail() {
		return defaultFromEmail;
	}

	public void setDefaultFromEmail(String defaultFromEmail) {
		this.defaultFromEmail = defaultFromEmail;
	}

	@Override
	public void sendMessage(String[] to, String[] cc, String[] bcc,
			String from, String subject, String body, boolean html,
			File[] attachments, File[] inlineFileName,
			Map<String, Object> properties) {

		MimeMessage mime = mailSender.createMimeMessage();

		boolean multipart = false;
		if ((attachments != null && attachments.length > 0)
				|| (inlineFileName != null && inlineFileName.length > 0)) {
			multipart = true;
		}

		try {
			MimeMessageHelper mhelper = new MimeMessageHelper(mime, multipart);

			setFrom(from, mhelper);

			setTo(to, mhelper);

			setCc(cc, mhelper);

			setBcc(bcc, mhelper);

			setSubject(subject, mhelper);

			setBody(body, html, mhelper, properties);

			setInlineContent(inlineFileName, mhelper);

			setAttachments(attachments, mhelper);

			mailSender.send(mime);

		} catch (MessagingException e) {
			LogFactory.getLog(getClass()).error(
					"Mail message could not be sent", e);
			e.printStackTrace();

			// TODO - add a queuing mechanism so that we can retry.
		}
	}

	private void setAttachments(File[] attachments, MimeMessageHelper mhelper)
			throws MessagingException {
		if (attachments == null) {
			return;
		}

		for (File attach : attachments) {
			mhelper.addAttachment(attach.getName(), attach);
		}
	}

	private void setInlineContent(File[] inlineFiles, MimeMessageHelper mhelper)
			throws MessagingException {
		if (inlineFiles == null) {
			return;
		}

		for (File inlineFile : inlineFiles) {
			mhelper.addInline(inlineFile.getName(), inlineFile);
		}
	}

	private void setBody(String body, boolean html, MimeMessageHelper mhelper,
			Map<String, Object> properties) throws MessagingException {

		if (!body.startsWith(TEMPLATE_PREFIX)) {
			mhelper.setText(body, html);
		} else {
			StringBuilder buffer = new StringBuilder(
					body.substring(TEMPLATE_PREFIX.length()));
			buffer.append(".vm");
			String rsrcFile = buffer.toString();

			mhelper.setText(VelocityEngineUtils.mergeTemplateIntoString(
					velocity, rsrcFile, properties), html);
		}
	}

	private void setFrom(String from, MimeMessageHelper mhelper)
			throws MessagingException {
		if (from == null) {
			from = defaultFromEmail;
		}
		mhelper.setFrom(from);
	}

	private void setTo(String[] to, MimeMessageHelper mhelper)
			throws MessagingException {
		for (String t : to) {
			mhelper.addTo(t);
		}
	}

	private void setCc(String[] cc, MimeMessageHelper mhelper)
			throws MessagingException {
		if (cc == null) {
			return;
		}
		for (String c : cc) {
			mhelper.addCc(c);
		}
	}

	private void setBcc(String[] bcc, MimeMessageHelper mhelper)
			throws MessagingException {
		if (bcc == null) {
			return;
		}
		for (String b : bcc) {
			mhelper.addBcc(b);
		}
	}

	private void setSubject(String subject, MimeMessageHelper mhelper)
			throws MessagingException {
		if (subject != null) {
			mhelper.setSubject(subject);
		}
	}
}
