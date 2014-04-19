/**
 * 
 */
package com.quikj.mw.core.business.bean;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Locale;

import com.keypoint.PngEncoder;
import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.quikj.mw.core.MiddlewareSecurityException;
import com.quikj.mw.core.business.CaptchaBean;
import com.quikj.mw.core.business.CaptchaType;

/**
 * @author Amit Chatterjee
 * 
 */
public class CaptchaBeanJCaptchaImpl implements CaptchaBean {

	private String captchaType = CaptchaType.LARGE.value();

	private ImageCaptchaService imageCaptchaService;

	public void setCaptchaType(String captchaType) {
		this.captchaType = captchaType;
	}

	public void init() {
		Color textColor = new Color(255, 255, 255);
		TextPaster textPaster = new SimpleTextPaster(5, 8, textColor);

		Color bgColor = new Color(0, 0, 0);

		BackgroundGenerator background;
		FontGenerator fontGenerator;
		if (CaptchaType.valueOf(captchaType) == CaptchaType.LARGE) {
			fontGenerator = new RandomFontGenerator(40, 50);
			background = new UniColorBackgroundGenerator(300, 100, bgColor);
		} else {
			fontGenerator = new RandomFontGenerator(20, 26);
			background = new UniColorBackgroundGenerator(150, 50, bgColor);
		}

		WordToImage wordToImage = new ComposedWordToImage(fontGenerator,
				background, textPaster);
		WordGenerator wordgen = new RandomWordGenerator(
				"abcdefghijklmnopqrstuvwxyz0123456789");

		CaptchaFactory[] factories = new CaptchaFactory[] { new GimpyFactory(
				wordgen, wordToImage) };
		CaptchaEngine imageEngine = new GenericCaptchaEngine(factories);

		DefaultManageableImageCaptchaService def = new DefaultManageableImageCaptchaService();
		def.setCaptchaEngine(imageEngine);

		imageCaptchaService = def;
	}

	@Override
	public byte[] getCaptchaPngImage(String captchaId, Locale locale) {
		BufferedImage challenge = imageCaptchaService.getImageChallengeForID(
				captchaId, locale);

		PngEncoder encoder = new PngEncoder(challenge);
		return encoder.pngEncode();
	}

	@Override
	public void validateCaptcha(String captchaId, String captcha) {
		if (captcha == null) {
			throw new MiddlewareSecurityException(
					"The captcha text has not been provided");
		}

		if (System.getProperty("test.env.proxy") != null) {
			return;
		}

		captcha = captcha.toLowerCase();

		boolean correct = imageCaptchaService.validateResponseForID(captchaId,
				captcha);
		if (!correct) {
			throw new MiddlewareSecurityException(
					"The captcha text did not match");
		}
	}
}
