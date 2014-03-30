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
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.quikj.mw.core.MiddlewareCoreException;
import com.quikj.mw.core.ValidationException;
import com.quikj.mw.core.business.ClientBean;
import com.quikj.mw.core.value.Authentication;
import com.quikj.mw.core.value.Client;
import com.quikj.mw.core.value.Domain;
import com.quikj.mw.core.value.Role;

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

	@Autowired
	private ClientBean clientBean;

	public void setClientBean(ClientBean clientBean) {
		this.clientBean = clientBean;
	}

	public ClientBeanTest() {
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

		clientBean.createClient(client);

		Client clientDb = clientBean.getClientByUserId("user1", "domain1");
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

		// Verify authentication
		Authentication auth = clientBean.authenticate("user1", "domain1",
				"A1b2c3d4");
		assertNotNull(auth);
		assertEquals(clientDb.getId(), auth.getId());
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

		// Now to the update operations

		client.setId(clientDb.getId());
		client.setUserId("user2");
		client.setFirstName("Test1");
		client.setLastName("User1");
		client.setEmail("user2@quik-j.com");
		client.setAdditionalInfo("Additional Information 2");
		client.setPassword("A1b2c3d4");
		client.setPhone1("9105551212");
		client.setPhone2("88885551212");

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
		Client client = new Client();
		
		client.setFirstName("Test1");
		client.setLastName("User1");
		client.setAdditionalInfo("Additional Information 2");
		client.setPassword("badpassword");
		client.setPhone1("9105551212");
		client.setPhone2("88885551212");
		
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
		
		client.setEmail("info@quik-j.com");
		
		try {
			clientBean.createClient(client);
			fail();
		} catch (ValidationException e) {
			// Expected
		}
		
		client.setPassword("A1b2c3d4");
		clientBean.createClient(client);
	}
}
