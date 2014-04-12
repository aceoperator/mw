/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.quikj.mw.core.dao.value.ClientDomainMap;
import com.quikj.mw.core.dao.value.ClientDomainRoleMap;
import com.quikj.mw.core.value.Authentication;
import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.Domain;
import com.quikj.mw.core.value.Role;
import com.quikj.mw.core.value.SecurityQuestion;

/**
 * @author amit
 * 
 */
public interface ClientDao {
	Authentication authenticate(@Param(value = "userId") String userId,
			@Param(value = "domain") String domain,
			@Param(value = "password") String password);
	
	Authentication authenticateByEmail(@Param(value = "email") String email,
			@Param(value = "domain") String domain,
			@Param(value = "password") String password);

	List<String> listRoles(@Param(value = "clientId") long clientId,
			@Param(value = "domainId") long domainId);

	void createClient(Client client);

	int createClientDomainMap(ClientDomainMap map);

	int deleteClient(long clientId);

	Client getClientByUserId(String userId);

	List<Domain> getClientDomains(long clientId);

	List<Role> getClientRoles(@Param(value = "clientId") long clientId,
			@Param(value = "domainId") long domainId);

	int updateClient(Client client);

	Client getClientById(long clientId);

	void deleteClientDomainMap(@Param(value = "clientId") long clientId,
			@Param(value = "domainName") String domainName);

	int createClientDomainRoleMap(ClientDomainRoleMap map);
	
	void createClientDomainRoleMap2(@Param(value = "clientId") long clientId,
			@Param(value = "domainId") long domainId,
			@Param(value = "roleName") String roleName);

	void deleteClientDomainRoleMap(@Param(value = "clientId") long clientId,
			@Param(value = "domainId") long domainId,
			@Param(value = "roleName") String roleName);

	void createDomain(Domain domain);

	void deleteDomain(long domainId);

	void updateDomain(Domain domain);

	Domain getDomainByName(String domainName);
	
	Domain getDomainById(long domainId);

	int changeOwnPassword(@Param(value = "userId") String userId,
			@Param(value = "oldPassword") String oldPassword,
			@Param(value = "newPassword") String newPassword);
	
	int changePassword(@Param(value = "userId") String userId,
			@Param(value = "newPassword") String newPassword);

	int clearSecurityQuestions(long clientId);

	int createSecurityQuestion(SecurityQuestion question);
	
	Long getClientId(@Param(value = "userId") String userId,
			@Param(value = "password") String password);

	List<SecurityQuestion> getSecurityQuestions(String userId);
}
