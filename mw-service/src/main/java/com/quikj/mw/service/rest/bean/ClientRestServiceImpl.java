/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest.bean;

import java.security.Principal;
import java.util.List;

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
import com.quikj.mw.core.value.Domain;
import com.quikj.mw.core.value.SecurityQuestion;
import com.quikj.mw.core.value.SecurityQuestions;
import com.quikj.mw.core.value.Success;
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

	public void setAuthManager(AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}

	@Override
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public Success changePassword(
			Principal principal,
			@RequestParam(value = "oldPassword", required = true) String oldPassword,
			@RequestParam(value = "newPassword", required = true) String newPassword) {
		clientBean.changeOwnPassword(principal.getName(), oldPassword,
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
	public Success login(@RequestParam("identifier") String identifier,
			@RequestParam("password") String password) {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				identifier, password);

		Authentication auth = authManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(auth);
		return new Success();
	}

	@Override
	@RequestMapping(value = "/reset/{identifier}", method = RequestMethod.POST)
	public Success resetPassword(@PathVariable String identifier,
			@RequestBody SecurityQuestions questions) {
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
	@RequestMapping(value = "/questions/{identifier}", method = RequestMethod.GET)
	public SecurityQuestions getSecurityQuestions(
			@PathVariable String identifier) {
		List<SecurityQuestion> questions;
		if (identifier.contains("@")) {
			questions = clientBean.getSecurityQuestionsByEmail(identifier);
		} else {
			questions = clientBean.getSecurityQuestionsByUserId(identifier);
		}

		return new SecurityQuestions(questions);
	}

	@Override
	@RequestMapping(value = "/questions/{identifier}", method = RequestMethod.PUT)
	public Success resetSecurityQuestions(@PathVariable String identifier,
			@RequestParam("password") String password, @RequestBody SecurityQuestions questions) {
		if (identifier.contains("@")) {
			clientBean.resetSecurityQuestionsByEmail(identifier, password,
					questions.getSecurityQuestions());
		} else {
			clientBean.resetSecurityQuestionsByUserId(identifier, password,
					questions.getSecurityQuestions());
		}

		return new Success();
	}
}
