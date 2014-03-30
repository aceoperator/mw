/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.dao.value;

/**
 * @author amit
 *
 */
public class ClientDomainRoleMap {

	private long clientDomainId;
	private String roleName;
	
	public ClientDomainRoleMap(long clientDomainId, String roleName) {
		this.clientDomainId = clientDomainId;
		this.roleName = roleName;
	}

	public long getClientDomainId() {
		return clientDomainId;
	}

	public String getRoleName() {
		return roleName;
	}
}
