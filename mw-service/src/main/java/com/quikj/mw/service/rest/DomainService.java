/**
 * $
 * Copyright 2011-2012 QUIK Computing. All rights reserved.
 */
package com.quikj.mw.service.rest;

import com.quikj.mw.core.value.Domain;
import com.quikj.mw.core.value.Success;

/**
 * @author amit
 *
 */
public interface DomainService {

	Success deleteDomain(long domainId);

	Success createDomain(Domain domain);

	Domain getDomainById(String domainName);

	Success modifyDomain(Domain domain);
}
