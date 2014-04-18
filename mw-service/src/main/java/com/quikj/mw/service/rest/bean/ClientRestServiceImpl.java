/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest.bean;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quikj.mw.core.business.ClientBean;
import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.SecurityQuestion;
import com.quikj.mw.core.value.SecurityQuestions;
import com.quikj.mw.core.value.Success;
import com.quikj.mw.service.MiddlewareServiceException;
import com.quikj.mw.service.framework.CaptchaUtil;
import com.quikj.mw.service.framework.MiddlewareGlobalProperties;
import com.quikj.mw.service.framework.MiddlewareUtil;
import com.quikj.mw.service.rest.ClientService;

/**
 * @author amit
 * 
 */
@Controller
@RequestMapping("/client")
public class ClientRestServiceImpl implements ClientService {
	@Autowired
	private ClientBean clientBean;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private MiddlewareGlobalProperties mwGlobalProperties;

	public void setMwGlobalProperties(
			MiddlewareGlobalProperties mwGlobalProperties) {
		this.mwGlobalProperties = mwGlobalProperties;
	}

	public void setAuthManager(AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}

	@Override
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public Success changePassword(
			@RequestParam(value = "oldPassword", required = true) String oldPassword,
			@RequestParam(value = "newPassword", required = true) String newPassword) {
		clientBean.changeOwnPassword(MiddlewareUtil.getUserId(), oldPassword,
				newPassword);
		return new Success();
	}

	@Override
	@RequestMapping(value = "/{clientId}", method = RequestMethod.DELETE)
	public Success deleteClient(@PathVariable long clientId) {
		clientBean.deleteClient(clientId);
		return new Success();
	}

	@Override
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Success createClient(@RequestBody Client client) {
		clientBean.createClient(client);
		return new Success();
	}

	@Override
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public Client getClientByUserId(@PathVariable String userId) {
		return clientBean.getClientByUserId(userId);
	}

	@Override
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public Success modifyClient(@RequestBody Client client) {
		clientBean.updateClient(client);
		return new Success();
	}

	@Override
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Success login(
			@RequestParam(value = "identifier", required = true) String identifier,
			@RequestParam(value = "password", required = true) String password) {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				identifier, password);

		Authentication auth = authManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(auth);
		return new Success();
	}

	@Override
	@RequestMapping(value = "/login", method = RequestMethod.DELETE)
	public Success logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return new Success();
	}

	// TODO test this
	@Override
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public Success resetPassword(
			HttpServletRequest request,
			@RequestParam(value = "identifier", required = true) String identifier,
			@RequestParam(value = "captcha", required = true) String captcha,
			@RequestBody SecurityQuestions questions) {

		CaptchaUtil.validateCaptcha(request, captcha);

		if (identifier.contains("@")) {
			clientBean.resetPasswordByEmail(identifier,
					questions.getSecurityQuestions());
		} else {
			clientBean.resetPasswordByUserId(identifier,
					questions.getSecurityQuestions());
		}
		return new Success();
	}

	@Override
	@RequestMapping(value = "/questions", method = RequestMethod.GET)
	public SecurityQuestions getSecurityQuestions(
			HttpServletRequest request,
			@RequestParam(value = "identifier", required = true) String identifier,
			@RequestParam(value = "captcha", required = false) String captcha) {

		if (mwGlobalProperties
				.getProperties()
				.getProperty(
						MiddlewareGlobalProperties.VALIDATE_CAPTCHA_ON_GET_SEC_QUESTIONS,
						"true").equals("true")) {
			if (captcha == null) {
				throw new MiddlewareServiceException(
						"No captcha value specified");
			}

			CaptchaUtil.validateCaptcha(request, captcha);
		}

		// TODO use a system variable to verify this
		if (captcha != null) {
			CaptchaUtil.validateCaptcha(request, captcha);
		}

		List<SecurityQuestion> questions;
		if (identifier.contains("@")) {
			questions = clientBean.getSecurityQuestionsByEmail(identifier);
		} else {
			questions = clientBean.getSecurityQuestionsByUserId(identifier);
		}

		return new SecurityQuestions(questions);
	}

	@Override
	@RequestMapping(value = "/questions", method = RequestMethod.PUT)
	public Success resetSecurityQuestions(
			@RequestBody SecurityQuestions questions) {
		clientBean.resetSecurityQuestions(MiddlewareUtil.getUserId(),
				questions.getSecurityQuestions());
		return new Success();
	}

	@Override
	@RequestMapping(value = "/profile", method = RequestMethod.PUT)
	public Success updateProfile(@RequestBody Client client) {
		client.setUserId(MiddlewareUtil.getUserId());
		clientBean.updateProfile(client);
		return new Success();
	}

	@Override
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public Client getProfile() {
		return clientBean.getProfileByUserId(MiddlewareUtil.getUserId());
	}
}
