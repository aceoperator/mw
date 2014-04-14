/**
 * 
 */
package com.quikj.mw.core.business.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.quikj.mw.core.MiddlewareCoreException;
import com.quikj.mw.core.ValidationException;
import com.quikj.mw.core.business.ClientBean;
import com.quikj.mw.core.value.Authentication;
import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.Domain;
import com.quikj.mw.core.value.Role;
import com.quikj.mw.core.value.SecurityQuestion;

/**
 * @author amit
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/META-INF/MwCoreSpringBase.xml",
		"/META-INF/MwCoreSpringBeans.xml", "/TestOverrides.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ClientBeanTest {

	private static SimpleSmtpServer smtpServer;
	
	@Autowired
	private ClientBean clientBean;

	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}

	public ClientBeanTest() {
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		smtpServer = SimpleSmtpServer.start();
	}

	@AfterClass
	public static void afterClass() {
		smtpServer.stop();
	}

	@Test
	public void testDomainOperations() {
		Domain domain = new Domain(0L, "domain1", "http://www.quik-j.com", null);
		clientBean.createDomain(domain);

		Domain domainDb = clientBean.getDomainByName("domain1");
		assertNotNull(domainDb);
		assertTrue(domainDb.getId() > 0L);
		assertEquals(domain.getName(), domainDb.getName());
		assertEquals(domain.getUrl(), domainDb.getUrl());

		domain.setId(domainDb.getId());
		domain.setName("domain1Changed");
		domain.setUrl("http://www.cafesip.org");
		clientBean.updateDomain(domain);

		domainDb = clientBean.getDomainByName("domain1Changed");
		assertNotNull(domainDb);
		assertTrue(domainDb.getId() > 0L);
		assertEquals(domain.getName(), domainDb.getName());
		assertEquals(domain.getUrl(), domainDb.getUrl());

		clientBean.deleteDomain(domain.getId());
		domainDb = clientBean.getDomainByName("domain1Changed");
		assertNull(domainDb);
	}

	@Test
	public void testClientOperations() {
		Domain domain1 = new Domain(0L, "domain1", "http://www.quik-j.com",
				null);
		clientBean.createDomain(domain1);

		Domain domain2 = new Domain(0L, "domain2", "http://www.cafesip.org",
				null);
		clientBean.createDomain(domain2);

		Client client = new Client();
		client.setUserId("user1");
		client.setFirstName("Test");
		client.setLastName("User");
		client.setEmail("user1@quik-j.com");
		client.setAdditionalInfo("Additional Information");
		client.setPassword("A1b2c3d4");
		client.setPhone1("9195551212");
		client.setPhone2("8005551212");
		client.setStreetAddress1("1000 Wall Street");
		client.setStreetAddress2("Suite 202");
		client.setCity("New York");
		client.setState("New York");
		client.setCountry("USA");
		client.setPostalCode("12345-7890");

		List<Role> roles = new ArrayList<Role>();
		roles.add(new Role(0L, "ADMIN"));
		roles.add(new Role(0L, "MANAGER"));

		client.getDomains().add(domain1);
		for (Role role : roles) {
			domain1.getRoles().add(role);
		}
		client.getDomains().add(domain2);
		for (Role role : roles) {
			domain2.getRoles().add(role);
		}

		client.setDefaultDomainName(domain1.getName());

		clientBean.createClient(client);

		Client clientDb = clientBean.getClientByUserId("user1");
		assertNotNull(clientDb);
		assertTrue(clientDb.getId() > 0L);
		assertEquals(client.getUserId(), clientDb.getUserId());
		assertNull(clientDb.getPassword());
		assertEquals(client.getFirstName(), clientDb.getFirstName());
		assertEquals(client.getLastName(), clientDb.getLastName());
		assertEquals(client.getEmail(), clientDb.getEmail());
		assertEquals(client.getAdditionalInfo(), clientDb.getAdditionalInfo());
		assertEquals(client.getPhone1(), clientDb.getPhone1());
		assertEquals(client.getPhone2(), clientDb.getPhone2());
		assertEquals(client.getStreetAddress1(), clientDb.getStreetAddress1());
		assertEquals(client.getStreetAddress2(), clientDb.getStreetAddress2());
		assertEquals(client.getCity(), clientDb.getCity());
		assertEquals(client.getState(), clientDb.getState());
		assertEquals(client.getCountry(), clientDb.getCountry());
		assertEquals(client.getPostalCode(), clientDb.getPostalCode());

		assertEquals(domain1.getName(), client.getDefaultDomainName());
		assertTrue(client.getDefaultDomainId() > 0L);

		assertEquals(2, clientDb.getDomains().size());

		Collections.sort(clientDb.getDomains(), new Comparator<Domain>() {
			@Override
			public int compare(Domain o1, Domain o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		assertEquals(domain1.getName(), clientDb.getDomains().get(0).getName());
		assertEquals(domain1.getUrl(), clientDb.getDomains().get(0).getUrl());
		assertEquals(2, clientDb.getDomains().get(0).getRoles().size());

		Collections.sort(clientDb.getDomains().get(0).getRoles(),
				new Comparator<Role>() {
					@Override
					public int compare(Role o1, Role o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		assertEquals(roles.get(0).getName(), clientDb.getDomains().get(1)
				.getRoles().get(0).getName());
		assertEquals(roles.get(1).getName(), clientDb.getDomains().get(1)
				.getRoles().get(1).getName());

		assertEquals(domain2.getName(), clientDb.getDomains().get(1).getName());
		assertEquals(domain2.getUrl(), clientDb.getDomains().get(1).getUrl());
		assertEquals(2, clientDb.getDomains().get(1).getRoles().size());

		Collections.sort(clientDb.getDomains().get(1).getRoles(),
				new Comparator<Role>() {
					@Override
					public int compare(Role o1, Role o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		assertEquals(roles.get(0).getName(), clientDb.getDomains().get(1)
				.getRoles().get(0).getName());
		assertEquals(roles.get(1).getName(), clientDb.getDomains().get(1)
				.getRoles().get(1).getName());

		clientDb = clientBean.getClientById(clientDb.getId());
		assertNotNull(clientDb);
		assertTrue(clientDb.getId() > 0L);
		assertEquals(client.getUserId(), clientDb.getUserId());
		assertNull(clientDb.getPassword());
		assertEquals(client.getFirstName(), clientDb.getFirstName());
		assertEquals(client.getLastName(), clientDb.getLastName());
		assertEquals(client.getEmail(), clientDb.getEmail());
		assertEquals(client.getAdditionalInfo(), clientDb.getAdditionalInfo());
		assertEquals(client.getPhone1(), clientDb.getPhone1());
		assertEquals(client.getPhone2(), clientDb.getPhone2());
		assertEquals(client.getStreetAddress1(), clientDb.getStreetAddress1());
		assertEquals(client.getStreetAddress2(), clientDb.getStreetAddress2());
		assertEquals(client.getCity(), clientDb.getCity());
		assertEquals(client.getState(), clientDb.getState());
		assertEquals(client.getCountry(), clientDb.getCountry());
		assertEquals(client.getPostalCode(), clientDb.getPostalCode());

		assertEquals(domain1.getName(), client.getDefaultDomainName());
		assertTrue(client.getDefaultDomainId() > 0L);

		assertEquals(2, clientDb.getDomains().size());

		Collections.sort(clientDb.getDomains(), new Comparator<Domain>() {
			@Override
			public int compare(Domain o1, Domain o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		assertEquals(domain1.getName(), clientDb.getDomains().get(0).getName());
		assertEquals(domain1.getUrl(), clientDb.getDomains().get(0).getUrl());
		assertEquals(2, clientDb.getDomains().get(0).getRoles().size());

		Collections.sort(clientDb.getDomains().get(0).getRoles(),
				new Comparator<Role>() {
					@Override
					public int compare(Role o1, Role o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		assertEquals(roles.get(0).getName(), clientDb.getDomains().get(1)
				.getRoles().get(0).getName());
		assertEquals(roles.get(1).getName(), clientDb.getDomains().get(1)
				.getRoles().get(1).getName());

		assertEquals(domain2.getName(), clientDb.getDomains().get(1).getName());
		assertEquals(domain2.getUrl(), clientDb.getDomains().get(1).getUrl());
		assertEquals(2, clientDb.getDomains().get(1).getRoles().size());

		Collections.sort(clientDb.getDomains().get(1).getRoles(),
				new Comparator<Role>() {
					@Override
					public int compare(Role o1, Role o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		assertEquals(roles.get(0).getName(), clientDb.getDomains().get(1)
				.getRoles().get(0).getName());
		assertEquals(roles.get(1).getName(), clientDb.getDomains().get(1)
				.getRoles().get(1).getName());

		// Now to the update operations

		client.setId(clientDb.getId());
		client.setUserId("user2");
		client.setFirstName("Test2");
		client.setLastName("User2");
		client.setEmail("user2@quik-j.com");
		client.setAdditionalInfo("Additional Information 2");
		client.setPassword("A1b2c3d4");
		client.setPhone1("9105551214");
		client.setPhone2("88885551214");
		client.setStreetAddress1("100 Bond Street");
		client.setStreetAddress2("Suite 2222");
		client.setCity("Chicago");
		client.setState("Illinois");
		client.setCountry("USA");
		client.setPostalCode("12346-7899");

		// Add a new domain, remove the old domain, modify roles in an existing
		// domain

		Domain domain3 = new Domain(0L, "domain3",
				"http://research.cafesip.org", null);
		clientBean.createDomain(domain3);

		// Add domain 3
		client.getDomains().add(domain3);
		for (Role role : roles) {
			domain3.getRoles().add(role);
		}

		// remove domain1
		client.getDomains().remove(0);

		// Add and remove roles from domain2
		domain2.getRoles().remove(0); // remove role admin
		domain2.getRoles().add(new Role(0L, "USER"));

		client.setDefaultDomainName(domain2.getName());

		clientBean.updateClient(client);

		clientDb = clientBean.getClientById(clientDb.getId());
		assertNotNull(clientDb);
		assertTrue(clientDb.getId() > 0L);
		assertFalse(client.getUserId() == clientDb.getUserId());
		assertNull(clientDb.getPassword());
		assertEquals(client.getFirstName(), clientDb.getFirstName());
		assertEquals(client.getLastName(), clientDb.getLastName());
		assertEquals(client.getEmail(), clientDb.getEmail());
		assertEquals(client.getAdditionalInfo(), clientDb.getAdditionalInfo());
		assertEquals(client.getPhone1(), clientDb.getPhone1());
		assertEquals(client.getPhone2(), clientDb.getPhone2());
		assertEquals(client.getStreetAddress1(), clientDb.getStreetAddress1());
		assertEquals(client.getStreetAddress2(), clientDb.getStreetAddress2());
		assertEquals(client.getCity(), clientDb.getCity());
		assertEquals(client.getState(), clientDb.getState());
		assertEquals(client.getCountry(), clientDb.getCountry());
		assertEquals(client.getPostalCode(), clientDb.getPostalCode());

		assertEquals(domain2.getName(), client.getDefaultDomainName());
		assertTrue(client.getDefaultDomainId() > 0L);

		assertEquals(2, clientDb.getDomains().size());

		Collections.sort(clientDb.getDomains(), new Comparator<Domain>() {
			@Override
			public int compare(Domain o1, Domain o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		assertEquals(domain2.getName(), clientDb.getDomains().get(0).getName());
		assertEquals(domain2.getUrl(), clientDb.getDomains().get(0).getUrl());

		Collections.sort(clientDb.getDomains().get(0).getRoles(),
				new Comparator<Role>() {
					@Override
					public int compare(Role o1, Role o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
		assertEquals("MANAGER", clientDb.getDomains().get(0).getRoles().get(0)
				.getName());
		assertEquals("USER", clientDb.getDomains().get(0).getRoles().get(1)
				.getName());

		assertEquals(domain3.getName(), clientDb.getDomains().get(1).getName());
		assertEquals(domain3.getUrl(), clientDb.getDomains().get(1).getUrl());

		Collections.sort(clientDb.getDomains().get(1).getRoles(),
				new Comparator<Role>() {
					@Override
					public int compare(Role o1, Role o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});

		assertEquals(roles.get(0).getName(), clientDb.getDomains().get(1)
				.getRoles().get(0).getName());
		assertEquals(roles.get(1).getName(), clientDb.getDomains().get(1)
				.getRoles().get(1).getName());

		// delete the client
		clientBean.deleteClient(clientDb.getId());
		try {
			clientDb = clientBean.getClientById(clientDb.getId());
			fail();
		} catch (MiddlewareCoreException e) {
			// Expected
		}
	}

	@Test
	public void testValidations() {
		Domain domain1 = new Domain(0L, "domain1", "http://www.quik-j.com",
				null);
		clientBean.createDomain(domain1);

		Domain domain2 = new Domain(0L, "domain2", "http://www.cafesip.org",
				null);
		clientBean.createDomain(domain2);

		Client client = new Client();

		client.setFirstName("Test1");
		client.setLastName("User1");
		client.setAdditionalInfo("Additional Information 2");
		client.setPassword("badpassword");
		client.setPhone1("1-9105551212A");
		client.setPhone2("1-9105551212A");

		client.getDomains().add(domain1);

		client.setDefaultDomainName("bogus");

		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q1", "a1"));
		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q1", "a2"));

		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.setUserId("user 2");
		client.setEmail("bademail");

		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.setUserId("user2");

		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.setEmail("bogus@quik-j.com");

		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.setPassword("A1b2c3d4");

		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.setDefaultDomainName(domain2.getName());

		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.setDefaultDomainName(domain1.getName());
		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.setPhone1("1-800-555-1212");
		client.setPhone2("1-800-555-1212");
		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.setPhone2("1-800-555-1213");
		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q3", "a3"));
		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.getSecurityQuestions().get(1).setQuestion("q2");

		clientBean.createClient(client);
		
		Authentication auth = clientBean.authenticate("user2", "domain1", client.getPassword());
		assertNotNull(auth);

		client.getSecurityQuestions().clear();
		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q1", "a4"));
		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q1", "a5"));

		try {
			clientBean.resetSecurityQuestions("user2", client.getPassword(),
					client.getSecurityQuestions());
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q3", "a6"));
		try {
			clientBean.resetSecurityQuestions("user2", client.getPassword(),
					client.getSecurityQuestions());
			fail();
		} catch (ValidationException e) {
			// Expected
		}

		client.getSecurityQuestions().get(1).setQuestion("q2");

		clientBean.resetSecurityQuestions("user2", client.getPassword(),
				client.getSecurityQuestions());
	}

	@Test
	public void testAuthentication() {
		Domain domain1 = new Domain(0L, "domain1", "http://www.quik-j.com",
				null);
		clientBean.createDomain(domain1);

		Domain domain2 = new Domain(0L, "domain2", "http://www.cafesip.org",
				null);
		clientBean.createDomain(domain2);

		Client client = new Client();
		client.setUserId("user1");
		client.setFirstName("Test");
		client.setLastName("User");
		client.setEmail("user1@quik-j.com");
		client.setAdditionalInfo("Additional Information");
		client.setPassword("A1b2c3d4");
		client.setPhone1("9195551212");
		client.setPhone2("8005551212");
		client.setStreetAddress1("1000 Wall Street");
		client.setStreetAddress2("Suite 202");
		client.setCity("New York");
		client.setState("New York");
		client.setCountry("USA");
		client.setPostalCode("12345-7890");

		List<Role> roles = new ArrayList<Role>();
		roles.add(new Role(0L, "ADMIN"));
		roles.add(new Role(0L, "MANAGER"));

		client.getDomains().add(domain1);
		for (Role role : roles) {
			domain1.getRoles().add(role);
		}
		client.getDomains().add(domain2);
		for (Role role : roles) {
			domain2.getRoles().add(role);
		}

		client.setDefaultDomainName(domain1.getName());

		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q1", "a1"));
		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q2", "a2"));
		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q3", "a3"));

		clientBean.createClient(client);

		Client clientDb = clientBean.getClientByUserId("user1");

		// Verify authentication
		Authentication auth = clientBean.authenticateByEmail(
				"user1@quik-j.com", "domain1", "A1b2c3d4");
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());
		assertEquals(clientDb.getUserId(), auth.getUserId());
		assertEquals("domain1", auth.getDomainName());
		assertEquals(clientDb.getDomains().get(0).getId(), auth.getDomainId());
		assertEquals(2, auth.getRoles().size());

		Collections.sort(auth.getRoles(), new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		
		auth = clientBean.authenticateByEmail(
				"user1@quik-j.com", null, "A1b2c3d4");
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());
		assertEquals(clientDb.getUserId(), auth.getUserId());
		assertEquals("domain1", auth.getDomainName());
		assertEquals(clientDb.getDomains().get(0).getId(), auth.getDomainId());
		assertEquals(2, auth.getRoles().size());

		Collections.sort(auth.getRoles(), new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		assertEquals(roles.get(0).getName(), auth.getRoles().get(0));
		assertEquals(roles.get(1).getName(), auth.getRoles().get(1));
		
		auth = clientBean.authenticate("user1", "domain1", "A1b2c3d4");
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());
		assertEquals(clientDb.getDomains().get(0).getId(), auth.getDomainId());
		assertEquals(clientDb.getUserId(), auth.getUserId());
		assertEquals("domain1", auth.getDomainName());
		assertEquals(2, auth.getRoles().size());

		Collections.sort(auth.getRoles(), new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		assertEquals(roles.get(0).getName(), auth.getRoles().get(0));
		assertEquals(roles.get(1).getName(), auth.getRoles().get(1));
		
		auth = clientBean.authenticate("user1", null, "A1b2c3d4");
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());
		assertEquals(clientDb.getDomains().get(0).getId(), auth.getDomainId());
		assertEquals(clientDb.getUserId(), auth.getUserId());
		assertEquals("domain1", auth.getDomainName());
		assertEquals(2, auth.getRoles().size());

		Collections.sort(auth.getRoles(), new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		assertEquals(roles.get(0).getName(), auth.getRoles().get(0));
		assertEquals(roles.get(1).getName(), auth.getRoles().get(1));

		// verify change password
		clientBean.changeOwnPassword("user1", "A1b2c3d4", "Abcd1234");

		auth = clientBean.authenticate("user1", "domain1", "Abcd1234");
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());

		clientBean.changePassword("user1", "A1b2c3d4");
		auth = clientBean.authenticate("user1", "domain1", "A1b2c3d4");
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());

		// Verify password reset
		String newPassword = clientBean.resetPassword("user1",
				client.getSecurityQuestions());
		assertNotNull(newPassword);
		assertTrue(newPassword.indexOf('-') == -1);

		assertEquals(1, smtpServer.getReceivedEmailSize());
		
		Iterator<?> iter = smtpServer.getReceivedEmail();
		SmtpMessage message = (SmtpMessage) iter.next();
		
		String[] to = message.getHeaderValues("To");
		assertEquals(1, to.length);
		assertEquals(client.getEmail(), to[0]);
		
		assertTrue(!message.getBody().contains("${"));
		
		auth = clientBean.authenticate("user1", "domain1", newPassword);
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());

		// Verify reset security questions
		client.getSecurityQuestions().clear();
		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q4", "a4"));
		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q5", "a5"));
		client.getSecurityQuestions().add(new SecurityQuestion(0L, "q6", "a6"));

		clientBean.resetSecurityQuestions("user1", newPassword,
				client.getSecurityQuestions());

		newPassword = clientBean.resetPassword("user1",
				client.getSecurityQuestions());
		assertNotNull(newPassword);
		assertTrue(newPassword.indexOf('-') == -1);

		auth = clientBean.authenticate("user1", "domain1", newPassword);
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());
		
		List<SecurityQuestion> questions = clientBean.getSecurityQuestions("user1");
		assertNotNull(questions);
		assertEquals(3, questions.size());
		
		for (SecurityQuestion question: questions) {
			assertNull(question.getAnswer());
		}
		
		questions = clientBean.getSecurityQuestionsByEmail("user1@quik-j.com");
		assertNotNull(questions);
		assertEquals(3, questions.size());
		
		for (SecurityQuestion question: questions) {
			assertNull(question.getAnswer());
		}
	}
}
