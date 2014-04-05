/**
 * 
 */
package com.quikj.mw.activiti.rest.bean;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.quikj.mw.activiti.rest.MiddlewareClient;
import com.quikj.mw.activiti.value.ProcessAttributes;

/**
 * @author amit
 * 
 */
public class MiddlewareClientImpl extends RestTemplate implements MiddlewareClient {
	private String mwServiceUrl;
	private String mwUser;
	private String mwPassword;
	private String mwHost;
	private int mwPort;

	public void init() {
		DefaultHttpClient client = new DefaultHttpClient();

		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
				mwUser, mwPassword);
		client.getCredentialsProvider().setCredentials(
				new AuthScope(mwHost, mwPort, AuthScope.ANY_REALM),
				credentials);
		HttpComponentsClientHttpRequestFactory reqFactory = new HttpComponentsClientHttpRequestFactory(
				client);
		setRequestFactory(reqFactory);		
	}

	@Override
	public ProcessAttributes invokeService(ProcessAttributes attributes) {
		return postForObject(mwServiceUrl
				+ "/bridge", attributes,
				ProcessAttributes.class);
	}

	public void setMwServiceUrl(String mwServiceUrl) {
		this.mwServiceUrl = mwServiceUrl;
	}

	public void setMwUser(String mwUser) {
		this.mwUser = mwUser;
	}

	public void setMwPassword(String mwPassword) {
		this.mwPassword = mwPassword;
	}

	public void setMwHost(String mwHost) {
		this.mwHost = mwHost;
	}

	public void setMwPort(int mwPort) {
		this.mwPort = mwPort;
	}
}
