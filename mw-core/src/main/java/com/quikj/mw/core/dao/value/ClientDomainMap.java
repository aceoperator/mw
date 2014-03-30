/**
 * Copyright 2011-2012 QUIK Computing All rights reserved.
 */
package com.quikj.mw.core.dao.value;

/**
 * @author amit
 *
 */
public class ClientDomainMap {

	private long id;
	private long clientId;
	private String domainName;
	
	public ClientDomainMap(long clientId, String domainName) {
		super();
		this.clientId = clientId;
		this.domainName = domainName;
	}

	public long getId() {
		return id;
	}

	public long getClientId() {
		return clientId;
	}
	
	public String getDomainName() {
		return domainName;
	}
}
