/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quikj.mw.core.business.ClientBean;
import com.quikj.mw.core.value.Domain;
import com.quikj.mw.core.value.Success;
import com.quikj.mw.service.rest.DomainService;

/**
 * @author amit
 * 
 */
@Controller
@RequestMapping("/domain")
public class DomainRestServiceImpl implements DomainService {
	@Autowired
	private ClientBean clientBean;

	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}

	@Override
	@RequestMapping(value = "/{domainId}", method = RequestMethod.DELETE)
	public @ResponseBody Success deleteDomain(@PathVariable long domainId) {
		clientBean.deleteDomain(domainId);
		return new Success();
	}

	@Override
	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody Success createDomain(@RequestBody Domain domain) {
		clientBean.createDomain(domain);
		return new Success();
	}

	@Override
	@RequestMapping(value = "/{domainName}", method = RequestMethod.GET)
	public @ResponseBody Domain getDomainById(@PathVariable String domainName) {
		return clientBean.getDomainByName(domainName);
	}

	@Override
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public @ResponseBody Success modifyDomain(@RequestBody Domain domain) {
		clientBean.updateDomain(domain);
		return new Success();
	}
}
