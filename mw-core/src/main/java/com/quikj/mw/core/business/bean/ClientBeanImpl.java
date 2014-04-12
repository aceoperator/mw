/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.business.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.quikj.mw.core.MiddlewareCoreException;
import com.quikj.mw.core.MiddlewareSecurityException;
import com.quikj.mw.core.ValidationException;
import com.quikj.mw.core.business.ClientBean;
import com.quikj.mw.core.dao.ClientDao;
import com.quikj.mw.core.dao.value.ClientDomainMap;
import com.quikj.mw.core.dao.value.ClientDomainRoleMap;
import com.quikj.mw.core.util.Validator;
import com.quikj.mw.core.value.Authentication;
import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.Domain;
import com.quikj.mw.core.value.Role;
import com.quikj.mw.core.value.SecurityQuestion;

/**
 * @author amit
 * 
 */
public class ClientBeanImpl implements ClientBean {

	private ClientDao clientDao;
	private int minimumSecurityQuestions = 3;

	public void setMinimumSecurityQuestions(int minimumSecurityQuestions) {
		this.minimumSecurityQuestions = minimumSecurityQuestions;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	@Override
	public Authentication authenticate(String userId, String domain,
			String password) {

		if (domain == null) {
			domain = ClientBean.DEFAULT_DOMAIN;
		}

		Authentication client = clientDao
				.authenticate(userId, domain, password);
		if (client == null) {
			throw new MiddlewareSecurityException("Authentication failed");
		}

		List<String> roles = clientDao.listRoles(client.getId(),
				client.getDomainId());
		client.setRoles(roles);

		return client;
	}

	@Override
	public Authentication authenticateByEmail(String email, String domain,
			String password) {

		if (domain == null) {
			domain = ClientBean.DEFAULT_DOMAIN;
		}

		Authentication client = clientDao.authenticateByEmail(email, domain,
				password);
		if (client == null) {
			throw new MiddlewareSecurityException("Authentication failed");
		}

		List<String> roles = clientDao.listRoles(client.getId(),
				client.getDomainId());
		client.setRoles(roles);

		return client;
	}

	@Override
	@RolesAllowed({ "SYS" })
	public void createClient(Client client) {

		validateClient(client, true);

		if (client.getDefaultDomainName() != null
				&& !client.getDefaultDomainName().isEmpty()) {
			validateAndSetDefaultDomainId(client);
		}

		clientDao.createClient(client);

		for (Domain domain : client.getDomains()) {
			ClientDomainMap domainMap = new ClientDomainMap(client.getId(),
					domain.getName());

			int affected = clientDao.createClientDomainMap(domainMap);
			if (affected == 0) {
				throw new MiddlewareCoreException("Invalid domain specified");
			}

			for (Role role : domain.getRoles()) {
				ClientDomainRoleMap roleMap = new ClientDomainRoleMap(
						domainMap.getId(), role.getName());
				affected = clientDao.createClientDomainRoleMap(roleMap);
				if (affected == 0) {
					throw new MiddlewareCoreException("Invalid role specified");
				}
			}
		}
	}

	private void validateAndSetDefaultDomainId(Client client) {
		boolean found = false;
		for (Domain domain: client.getDomains()) {
			if (domain.getName().equals(client.getDefaultDomainName())) {
				found = true;
				break;
			}
		}
		
		if (!found) {
			throw new ValidationException("The default domain is not found in the domain list");
		}
		
		client.setDefaultDomainName(Validator.validateName(client.getDefaultDomainName(), "Default Domain Name"));
		Domain domain = clientDao.getDomainByName(client.getDefaultDomainName());
		if (domain == null) {
			throw new ValidationException("The default domain was not found");
		}
		
		client.setDefaultDomainId(domain.getId());
	}

	@Override
	@RolesAllowed({ "SYS" })
	public void updateClient(Client client) {

		validateClient(client, false);
		
		if (client.getDefaultDomainName() != null
				&& !client.getDefaultDomainName().isEmpty()) {
			validateAndSetDefaultDomainId(client);
		}

		Client oldClient = getClientById(client.getId());
		if (oldClient == null) {
			throw new MiddlewareCoreException("The client was not found");
		}

		int affected = clientDao.updateClient(client);
		if (affected == 0) {
			throw new MiddlewareCoreException("Error updating client");
		}

		List<Domain> unchangedDomains = new ArrayList<>(oldClient.getDomains());

		// First, find the new domains that were found in this request that were
		// not present in the older definition in the database
		List<Domain> newDomains = compareDomains(oldClient.getDomains(),
				client.getDomains());
		for (Domain domain : newDomains) {
			ClientDomainMap map = new ClientDomainMap(client.getId(),
					domain.getName());
			clientDao.createClientDomainMap(map);

			for (Role role : domain.getRoles()) {
				ClientDomainRoleMap roleMap = new ClientDomainRoleMap(
						map.getId(), role.getName());
				clientDao.createClientDomainRoleMap(roleMap);
			}
		}

		// Next, remove the domains that were in the database that are no longer
		// in this request
		List<Domain> removedDomains = compareDomains(client.getDomains(),
				oldClient.getDomains());
		for (Domain domain : removedDomains) {
			clientDao.deleteClientDomainMap(client.getId(), domain.getName());
			unchangedDomains.remove(domain);
		}

		// Finally, manage the client-domain-role map as needed for the entries
		// that were neither added nor removed
		for (Domain domain : unchangedDomains) {
			Domain newDomain = findDomain(client.getDomains(), domain.getName());

			List<Role> newRoles = compareRoles(domain.getRoles(),
					newDomain.getRoles());

			for (Role role : newRoles) {
				clientDao.createClientDomainRoleMap2(client.getId(),
						domain.getId(), role.getName());
			}

			List<Role> removedRoles = compareRoles(newDomain.getRoles(),
					domain.getRoles());
			for (Role role : removedRoles) {
				clientDao.deleteClientDomainRoleMap(client.getId(),
						domain.getId(), role.getName());
			}
		}
	}

	private Domain findDomain(List<Domain> domains, String domainName) {
		for (Domain domain : domains) {
			if (domain.getName().equals(domainName)) {
				return domain;
			}
		}

		return null;
	}

	// Returns the domains from list2 that are not found in list1
	private List<Domain> compareDomains(List<Domain> list1, List<Domain> list2) {
		List<Domain> domains = new ArrayList<Domain>();

		for (Domain d2 : list2) {
			boolean found = false;
			for (Domain d1 : list1) {
				if (d1.getName().equals(d2.getName())) {
					found = true;
					break;
				}
			}

			if (!found) {
				domains.add(d2);
			}
		}

		return domains;
	}

	// Returns the roles from list2 that are not found in list1
	private List<Role> compareRoles(List<Role> list1, List<Role> list2) {
		List<Role> roles = new ArrayList<Role>();

		for (Role r2 : list2) {
			boolean found = false;
			for (Role r1 : list1) {
				if (r1.getName().equals(r2.getName())) {
					found = true;
					break;
				}
			}

			if (!found) {
				roles.add(r2);
			}
		}

		return roles;
	}

	@Override
	@RolesAllowed({ "SYS" })
	public void deleteClient(long clientId) {
		int affected = clientDao.deleteClient(clientId);
		if (affected == 0) {
			throw new MiddlewareCoreException("The client was not found");
		}
	}

	@Override
	public Client getClientByUserId(String userId) {
		Client client = clientDao.getClientByUserId(userId);
		if (client == null) {
			throw new MiddlewareCoreException("The client was not found");
		}
		
		if (client.getDefaultDomainId() != null && client.getDefaultDomainId() > 0L) {
			Domain domain = clientDao.getDomainById(client.getDefaultDomainId());
			client.setDefaultDomainName(domain.getName());
		}
		
		List<Domain> domains = clientDao.getClientDomains(client.getId());

		for (Domain d : domains) {
			client.getDomains().add(d);
			List<Role> roles = clientDao.getClientRoles(client.getId(),
					d.getId());
			d.setRoles(roles);
		}

		return client;
	}

	@Override
	public Client getClientById(long clientId) {
		Client client = clientDao.getClientById(clientId);
		if (client == null) {
			throw new MiddlewareCoreException("The client was not found");
		}

		if (client.getDefaultDomainId() != null && client.getDefaultDomainId() > 0L) {
			Domain domain = clientDao.getDomainById(client.getDefaultDomainId());
			client.setDefaultDomainName(domain.getName());
		}
		
		List<Domain> domains = clientDao.getClientDomains(client.getId());

		for (Domain d : domains) {
			client.getDomains().add(d);
			List<Role> roles = clientDao.getClientRoles(client.getId(),
					d.getId());
			d.setRoles(roles);
		}

		return client;
	}

	@Override
	@RolesAllowed({ "SYS" })
	public void createDomain(Domain domain) {
		validateDomain(domain, true);
		clientDao.createDomain(domain);
	}

	@Override
	@RolesAllowed({ "SYS" })
	public void deleteDomain(long domainId) {
		clientDao.deleteDomain(domainId);
	}

	@Override
	@RolesAllowed({ "SYS" })
	public void updateDomain(Domain domain) {
		validateDomain(domain, false);
		clientDao.updateDomain(domain);
	}

	@Override
	@RolesAllowed({ "SYS" })
	public Domain getDomainByName(String domainName) {
		return clientDao.getDomainByName(domainName);
	}
	
	@Override
	@RolesAllowed({ "SYS" })
	public Domain getDomainById(long domainId) {
		return clientDao.getDomainById(domainId);
	}

	@Override
	public void changeOwnPassword(String userId, String oldPassword,
			String newPassword) {
		newPassword = Validator.validatePassword(newPassword);
		int affected = clientDao.changeOwnPassword(userId, oldPassword,
				newPassword);
		if (affected == 0) {
			throw new MiddlewareSecurityException(
					"The password change failed because the user name and/or the password is incorrect");
		}
	}
	
	@Override
	@RolesAllowed({ "SYS" })
	public void changePassword(String userId, String newPassword) {
		newPassword = Validator.validatePassword(newPassword);
		int affected = clientDao.changePassword(userId, newPassword);
		if (affected == 0) {
			throw new MiddlewareSecurityException(
					"The password change failed because the user name is incorrect");
		}
	}

	private void validateDomain(Domain domain, boolean createOperation) {
		domain.setName(Validator.validateName(domain.getName(), "Domain Name"));

		if (!createOperation) {
			// update operation
			if (domain.getId() <= 0L) {
				throw new ValidationException(
						"The domain id has not been specified");
			}
		}
	}
	
	@Override
	public void setSecurityQuestions(String userId, String password, List<SecurityQuestion> securityQuestions) {
		Long clientId = clientDao.getClientId(userId, password);
		if (clientId == null) {
			throw new MiddlewareSecurityException("Authentication failed");
		}
		
		validateSecurityQuestions(securityQuestions);
		
		// Remove the previous questions set by this user
		clientDao.clearSecurityQuestions(clientId);
		
		for (SecurityQuestion question: securityQuestions) {
			question.setClientId(clientId);
			clientDao.createSecurityQuestion(question);
		}
	}

	private void validateClient(Client client, boolean createOperation) {

		client.setUserId(Validator.validateName(client.getUserId(), "User Id"));

		if (createOperation) {
			Validator.validatePassword(client.getPassword());
		}

		Validator.validateEmail(client.getEmail(), "Email");
		
		
		if (client.getPhone1() != null) {
			client.setPhone1(Validator.validatePhoneNumber(client.getPhone1(), "Phone Number 1"));
		}
		
		if (client.getPhone2() != null) {
			client.setPhone2(Validator.validatePhoneNumber(client.getPhone2(), "Phone Number 2"));
		}
		
		if (client.getPhone1() != null && client.getPhone2() != null) {
			if (client.getPhone1().equals(client.getPhone2())) {
				throw new ValidationException("The phone numbers are the same");
			}
		}		
	}

	private void validateSecurityQuestions(List<SecurityQuestion> questions) {
		if (questions.size() < minimumSecurityQuestions ) {
			throw new ValidationException("You must provide at least " + minimumSecurityQuestions + " questions");
		}
		
		List<String> previousQuestions = new ArrayList<>();
		for (SecurityQuestion security: questions) {
			if (security.getQuestion() == null || security.getQuestion().trim().isEmpty()) {
				throw new ValidationException("A question cannot be empty");
			}
				
			security.setQuestion(security.getQuestion().trim());
			
			if (security.getAnswer() == null || security.getAnswer().trim().isEmpty()) {
				throw new ValidationException("An answer cannot be empty");
			}
			
			security.setAnswer(security.getAnswer().trim());
			
			for (String question: previousQuestions) {
				if (security.getQuestion().equalsIgnoreCase(question)) {
					throw new ValidationException("Duplicate question");
				}
			}
			
			previousQuestions.add(security.getQuestion());
		}
	}
}
