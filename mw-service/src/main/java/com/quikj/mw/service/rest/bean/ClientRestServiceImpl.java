/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest.bean;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.quikj.mw.core.business.ClientBean;
import com.quikj.mw.core.value.Client;
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

	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}

	@Override
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public Success changePassword(
			Principal principal,
			@RequestParam(value = "oldPassword", required = true) String oldPassword,
			@RequestParam(value = "newPassword", required = true) String newPassword) {
		clientBean.changeOwnPassword(principal.getName(), oldPassword, newPassword);
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
	public Success modifyUser(@RequestBody Client client) {
		clientBean.updateClient(client);
		return new Success();
	}
}
