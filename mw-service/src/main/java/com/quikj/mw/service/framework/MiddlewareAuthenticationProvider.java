/**
 * 
 */
package com.quikj.mw.service.framework;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;

import com.quikj.mw.core.business.ClientBean;

/**
 * @author Amit Chatterjee
 * 
 */
public class MiddlewareAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private ClientBean clientBean;

	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}

	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {

		try {
			String name = auth.getName();
			String[] tokens = name.split(MiddlewareUtil.USERNAME_DELIMITER);
			String userName = tokens[0];
			String domainName = null;
			if (tokens.length > 1) {
				domainName = tokens[1];
			}

			com.quikj.mw.core.value.Authentication authentication;
			tokens = userName.split("@");
			if (tokens.length > 1) {
				authentication = clientBean.authenticateByEmail(userName,
						domainName, (String) auth.getCredentials());
			} else {
				authentication = clientBean.authenticate(userName, domainName,
						(String) auth.getCredentials());
			}

			List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
			for (String role : authentication.getRoles()) {
				roles.add(new GrantedAuthorityImpl(role));
			}

			User u = new User(
					authentication.getUserId() + ","
							+ authentication.getDomainName(),
					(String) auth.getCredentials(), true, true, true, true,
					roles);
			return new UsernamePasswordAuthenticationToken(u,
					auth.getCredentials(), roles);
		} catch (Exception e) {
			throw new BadCredentialsException("Authentication Failed!", e);
		}
	}

	@Override
	public boolean supports(Class<? extends Object> authObj) {
		if (authObj.isAssignableFrom(UsernamePasswordAuthenticationToken.class)) {
			return true;
		}
		return false;
	}
}
