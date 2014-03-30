/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.quikj.mw.core.value.Authentication;
import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.Domain;


/**
 * @author amit
 * 
 */
public interface ClientBean {
	
	public static final String DEFAULT_DOMAIN = "default";

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Authentication authenticate(String userId, String domain, String password);
	
	@Transactional(propagation = Propagation.REQUIRED)
	void createClient(Client client);

	@Transactional(propagation = Propagation.REQUIRED)
	void updateClient(Client client);

	@Transactional(propagation = Propagation.REQUIRED)
	void deleteClient(long clientId);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Client getClientByUserId(String userId, String domain);

	
	@Transactional(propagation = Propagation.REQUIRED)
	void createDomain(Domain domain);

	@Transactional(propagation = Propagation.REQUIRED)
	void deleteDomain(long domainId);

	@Transactional(propagation = Propagation.REQUIRED)
	void updateDomain(Domain domain);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Domain getDomainByName(String domainName);

	@Transactional(propagation = Propagation.REQUIRED)
	void changePassword(String userId, String oldPassword, String newPassword);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Client getClientById(long clientId);
}