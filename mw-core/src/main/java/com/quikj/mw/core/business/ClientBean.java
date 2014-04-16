/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.quikj.mw.core.value.Authentication;
import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.Domain;
import com.quikj.mw.core.value.SecurityQuestion;


/**
 * @author amit
 * 
 */
public interface ClientBean {
	
	public static final String DEFAULT_DOMAIN = "DEFAULT";

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Authentication authenticate(String userId, String domain, String password);
	
	@Transactional(propagation = Propagation.REQUIRED)
	void createClient(Client client);

	@Transactional(propagation = Propagation.REQUIRED)
	void updateClient(Client client);

	@Transactional(propagation = Propagation.REQUIRED)
	void deleteClient(long clientId);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Client getClientByUserId(String userId);
	
	@Transactional(propagation = Propagation.REQUIRED)
	void createDomain(Domain domain);

	@Transactional(propagation = Propagation.REQUIRED)
	void deleteDomain(long domainId);

	@Transactional(propagation = Propagation.REQUIRED)
	void updateDomain(Domain domain);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Domain getDomainByName(String domainName);

	@Transactional(propagation = Propagation.REQUIRED)
	void changeOwnPassword(String userId, String oldPassword, String newPassword);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Client getClientById(long clientId);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Authentication authenticateByEmail(String email, String domain,
			String password);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Domain getDomainById(long domainId);

	@Transactional(propagation = Propagation.REQUIRED)
	void changePassword(String userId, String newPassword);

	@Transactional(propagation = Propagation.REQUIRED)
	void resetSecurityQuestionsByUserId(String userId, String password,
			List<SecurityQuestion> securityQuestions);

	@Transactional(propagation = Propagation.REQUIRED)
	String resetPasswordByUserId(String userId, List<SecurityQuestion> questions);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	List<SecurityQuestion> getSecurityQuestionsByUserId(String userId);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	List<SecurityQuestion> getSecurityQuestionsByEmail(String email);

	@Transactional(propagation = Propagation.REQUIRED)
	String resetPasswordByEmail(String email, List<SecurityQuestion> questions);

	@Transactional(propagation = Propagation.REQUIRED)
	void resetSecurityQuestionsByEmail(String email, String password,
			List<SecurityQuestion> securityQuestions);

	@Transactional(propagation = Propagation.REQUIRED)
	void updateProfile(Client client);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Client getProfileByUserId(String userId);

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	Client getProfileByEmail(String email);
}