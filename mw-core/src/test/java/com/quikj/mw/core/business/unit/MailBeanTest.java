/**
 * 
 */
package com.quikj.mw.core.business.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.quikj.mw.core.business.MailBean;

/**
 * @author amit
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/META-INF/MwCoreSpringBase.xml",
		"/META-INF/MwCoreSpringBeans.xml", "/TestOverrides.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class MailBeanTest {

	private static SimpleSmtpServer smtpServer;

	@Autowired
	private MailBean mailBean;

	public MailBeanTest() {
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		smtpServer = SimpleSmtpServer.start();
	}

	@AfterClass
	public static void afterClass() {
		smtpServer.stop();
	}

	public MailBean getMailBean() {
		return mailBean;
	}

	public void setMailBean(MailBean mailBean) {
		this.mailBean = mailBean;
	}

	@Test
	public void testMailOperations() {
		String[] to = { "sales@quik-j.com", "info@quik-j.com" };
		String[] cc = { "webmaster@quik-j.com" };
		mailBean.sendMessage(to, cc, null, null, "This is a test email",
				"Hello, this is a test", false, null, null, null);

		SmtpMessage email = getLastMessage();
		assertNotNull(email);
		assertEquals("Hello, this is a test", email.getBody());
		assertEquals("This is a test email", email.getHeaderValue("Subject"));

		to = email.getHeaderValues("To");
		assertEquals(2, to.length);
		Arrays.sort(to);
		assertEquals("info@quik-j.com", to[0]);
		assertEquals("sales@quik-j.com", to[1]);

		cc = email.getHeaderValues("Cc");
		assertEquals(1, cc.length);
		Arrays.sort(cc);
		assertEquals("webmaster@quik-j.com", cc[0]);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("prop1", "hello");
		properties.put("prop2", "world");
		mailBean.sendMessage(to, cc, null, null, "This is a test email",
				"template:test/mail/test", false, null, null, properties);

		email = getLastMessage();
		assertTrue(email.getBody().startsWith("This is a test."));
		assertTrue(email.getBody().contains("hello"));
		assertTrue(email.getBody().contains("world"));
	}

	private SmtpMessage getLastMessage() {
		SmtpMessage ret = null;
		Iterator<?> iter = smtpServer.getReceivedEmail();
		while (iter.hasNext()) {
			ret = (SmtpMessage) iter.next();
		}

		// System.out.println(ret);
		return ret;
	}
}
