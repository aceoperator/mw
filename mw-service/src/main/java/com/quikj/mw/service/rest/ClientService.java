/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest;

import javax.servlet.http.HttpServletRequest;

import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.SecurityQuestions;
import com.quikj.mw.core.value.Success;

/**
 * @author amit
 * 
 */
public interface ClientService {

	Success changePassword(String oldPassword, String newPassword);

	Success deleteClient(long clientId);

	Success createClient(Client client);

	Client getClientByUserId(String userId);

	Success modifyClient(Client client);

	Success login(String identifier, String password);

	Success resetPassword(HttpServletRequest request, String identifier,
			String captcha, SecurityQuestions questions);

	SecurityQuestions getSecurityQuestions(HttpServletRequest request,
			String identifier, String captcha);

	Success resetSecurityQuestions(SecurityQuestions questions);

	Success updateProfile(Client client);

	Client getProfile();

	Success logout();
}