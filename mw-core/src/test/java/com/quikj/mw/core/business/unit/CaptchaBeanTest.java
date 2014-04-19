/**
 * 
 */
package com.quikj.mw.core.business.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.quikj.mw.core.business.CaptchaBean;
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
public class CaptchaBeanTest {

	@Autowired
	private CaptchaBean captchaBeanLarge;
	
	@Autowired
	private CaptchaBean captchaBeanSmall;

	public void setCaptchaBeanSmall(CaptchaBean captchaBeanSmall) {
		this.captchaBeanSmall = captchaBeanSmall;
	}
	
	public void setCaptchaBeanLarge(CaptchaBean captchaBeanLarge) {
		this.captchaBeanLarge = captchaBeanLarge;
	}

	@Test
	public void testCaptchaOperations() {
		byte[] png = captchaBeanLarge.getCaptchaPngImage("123", Locale.US);
		assertNotNull(png);
		assertTrue(png.length > 10);
		
		png = captchaBeanSmall.getCaptchaPngImage("123", Locale.US);
		assertNotNull(png);
		assertTrue(png.length > 10);
	}
}
