/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest;

import java.security.Principal;

import com.quikj.mw.core.value.Client;
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

	Client getClientByUserId(String domain, String userId);
	
	Client getClientById(long clientId);

	Success modifyUser(Client client);
}