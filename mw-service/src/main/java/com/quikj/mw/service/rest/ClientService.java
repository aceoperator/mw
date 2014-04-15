/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest;

import java.security.Principal;

import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.SecurityQuestions;
import com.quikj.mw.core.value.Success;


/**
 * @author amit
 * 
 */
public interface ClientService {

	Success changePassword(Principal principal, String oldPassword,
			String newPassword);

	Success deleteClient(long clientId);

	Success createClient(Client client);

	Client getClientByUserId(String userId);
	
	Success modifyClient(Client client);

	Success login(String identifier, String password);

	Success resetPassword(String identifier, SecurityQuestions questions);

	SecurityQuestions getSecurityQuestions(String identifier);

	Success resetSecurityQuestions(String identifier, String password,
			SecurityQuestions questions);
}