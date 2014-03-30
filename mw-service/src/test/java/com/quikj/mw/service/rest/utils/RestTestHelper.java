/**
 * 
 */
package com.quikj.mw.service.rest.utils;

import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author amit
 * 
 */
public class RestTestHelper {

	public static void authenticate(RestTemplate restTemplate, String userName, String password)
			throws HttpException, IOException {

		HttpClient client = new HttpClient();
		Credentials credentials1 = new UsernamePasswordCredentials(
				userName, password);
		client.getState().setCredentials(AuthScope.ANY, credentials1);

		String url = "http://localhost:8080/mw";
		HttpMethod method = new GetMethod(url);
		client.executeMethod(method);
		method.releaseConnection();

		CommonsClientHttpRequestFactory reqFactory = new CommonsClientHttpRequestFactory(
				client);
		restTemplate.setRequestFactory(reqFactory);
	}
}
