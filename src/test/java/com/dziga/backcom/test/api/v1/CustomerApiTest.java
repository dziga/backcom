package com.dziga.backcom.test.api.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;


import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.dziga.backcom.api.v1.CustomerApi;
import com.dziga.backcom.test.TestConstants;
import com.github.tomakehurst.wiremock.common.LocalNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SuppressWarnings("unused")
public class CustomerApiTest {
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(8089).notifier(
	        new Slf4jNotifier(true)));

	@Test
	public void customerApiGet() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
	
		CustomerApi customer = new CustomerApi();
		customer.setCustomerId(3);
		
		customer.getCustomer();
		
		Assert.assertEquals(3, customer.getCustomerId());
		Assert.assertEquals("John", customer.getCustomerFirstName());
		Assert.assertEquals("Doe", customer.getCustomerLastName());
		Assert.assertEquals("Backer street 3", customer.getCustomerStreet());
		Assert.assertEquals("London", customer.getCustomerCity());
	}
	
	@Test
	public void customerApiCreate() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
	
		CustomerApi customer = new CustomerApi();
		customer.setCustomerFirstName("John");
		customer.setCustomerLastName("Doe");
		customer.setCustomerStreet("Backer street 3");
		customer.setCustomerCity("London");
		
		customer.createNewCustomer();
		
		Assert.assertEquals("John", customer.getCustomerFirstName());
		Assert.assertEquals("Doe", customer.getCustomerLastName());
		Assert.assertEquals("Backer street 3", customer.getCustomerStreet());
		Assert.assertEquals("London", customer.getCustomerCity());
	}
	
	@Test
	public void customerApiEdit() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
	
		CustomerApi customer = new CustomerApi();
		customer.setCustomerId(2);
		customer.setCustomerFirstName("John");
		customer.setCustomerLastName("Doe");
		customer.setCustomerStreet("Backer street 3");
		customer.setCustomerCity("London");
		
		customer.editCustomer();
		
		Assert.assertEquals(2, customer.getCustomerId());
		Assert.assertEquals("John", customer.getCustomerFirstName());
		Assert.assertEquals("Doe", customer.getCustomerLastName());
		Assert.assertEquals("Backer street 3", customer.getCustomerStreet());
		Assert.assertEquals("London", customer.getCustomerCity());
	}
	
	@Test
	public void customerApiDelete() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
	
		CustomerApi customer = new CustomerApi();
		customer.setCustomerId(5);
		
		Assert.assertTrue(customer.deleteCustomer());
	}
	
	@Before
	public void setUp() {
		
		stubFor(get(urlEqualTo("/v1/customers/3")).withHeader("Accept", equalTo("application/xml"))
				.willReturn(
	            aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "application/xml")
	                .withBody(
	                    TestConstants.XML_HEADER 
	                            + "<Customer>"
	                            + "<id>3</id>"
	                            + "<firstname>John</firstname>"
	                            + "<lastname>Doe</lastname>" 
	                            + "<street>Backer street 3</street>"
	                            + "<city>London</city>"
	                            + "</Customer>")));
		
		stubFor(post(urlEqualTo("/v1/customers")).withHeader("Accept", equalTo("application/xml"))
				.withRequestBody(matching(
								".*<Customer>.*"
	                            + ".*<firstname>John</firstname>"
                        		+ ".*<lastname>Doe</lastname>.*" 
	                            + ".*<street>Backer street 3</street>.*"
	                            + ".*<city>London</city>.*" 
	                            + ".*</Customer>.*"
						))
				.willReturn(
	            aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "application/xml")
	                .withBody(
	                    TestConstants.XML_HEADER 
	                            + "<Customer>"
	                            + "<id>1</id>"
	                            + "<firstname>John</firstname>"
	                            + "<lastname>Doe</lastname>" 
	                            + "<street>Backer street 3</street>"
	                            + "<city>London</city>"
	                            + "</Customer>")));
		
		stubFor(put(urlEqualTo("/v1/customers/2")).withHeader("Accept", equalTo("application/xml"))
				.withRequestBody(matching(
								".*<Customer>.*"
								+ ".*<id>2</id>.*"
	                            + ".*<firstname>John</firstname>"
                        		+ ".*<lastname>Doe</lastname>.*" 
	                            + ".*<street>Backer street 3</street>.*"
	                            + ".*<city>London</city>.*" 
	                            + ".*</Customer>.*"
						))
				.willReturn(
	            aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "application/xml")
	                .withBody(
	                    TestConstants.XML_HEADER 
	                            + "<Customer>"
	                            + "<id>2</id>"
	                            + "<firstname>John</firstname>"
	                            + "<lastname>Doe</lastname>" 
	                            + "<street>Backer street 3</street>"
	                            + "<city>London</city>"
	                            + "</Customer>")));
		stubFor(delete(urlEqualTo("/v1/customers/5")).withHeader("Accept", equalTo("application/xml"))
				.willReturn(
	            aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "application/xml")
	    ));
	}
	
}
