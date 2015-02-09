package com.dziga.backcom.test.api.v2;

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


import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.dziga.backcom.api.v2.CustomerApi;
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

	@Before
	public void setUp() {
		stubFor(post(urlEqualTo("/v2/customers")).withHeader("Accept", equalTo("application/xml"))
				.withRequestBody(matching(
								".*<Customer>.*"
	                            + ".*<FirstName>John</FirstName>.*"
                        		+ ".*<LastName>Doe</LastName>.*" 
                        		+ ".*<SignedContractDate>09-10-2015</SignedContractDate>.*"
	                            + ".*<Street>Backer street</Street>.*"
	                            + ".*<StreetNumber>3</StreetNumber>.*"
	                            + ".*<City>London</City>.*"
	                            + ".*<PostalCode>20002</PostalCode>.*"
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
	                            + "<FirstName>John</FirstName>"
	                            + "<LastName>Doe</LastName>"
	                            + "<SignedContractDate>09-10-2015</SignedContractDate>"
	                            + "<Street>Backer street</Street>"
	                            + "<StreetNumber>3</StreetNumber>"
	                            + "<City>London</City>"
	                            + "<PostalCode>200002</PostalCode>"
	                            + "</Customer>")));
	}
	
	@Test
	public void customerApiPost() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
	
		CustomerApi customer = new CustomerApi();
		customer.setCustomerFirstName("John");
		customer.setCustomerLastName("Doe");
		customer.setCustomerSignedContractDate("09-10-2015");
		customer.setCustomerStreet("Backer street");
		customer.setCustomerStreetNumber(3);
		customer.setCustomerCity("London");
		customer.setCustomerPostalCode(20002);
		
		customer.createNewCustomer();
		
		Assert.assertEquals("John", customer.getCustomerFirstName());
		Assert.assertEquals("Doe", customer.getCustomerLastName());
		Assert.assertEquals("09-10-2015", customer.getCustomerSignedContractDate());
		Assert.assertEquals("Backer street", customer.getCustomerStreet());
		Assert.assertEquals(3, customer.getCustomerStreetNumber());
		Assert.assertEquals("London", customer.getCustomerCity());
		Assert.assertEquals(Integer.valueOf(200002), customer.getCustomerPostalCode());
	}
}
