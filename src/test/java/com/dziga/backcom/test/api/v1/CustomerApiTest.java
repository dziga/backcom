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

import org.junit.Rule;
import org.junit.Test;

import com.dziga.backcom.api.v1.CustomerApi;
import com.dziga.backcom.test.TestConstants;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SuppressWarnings("unused")
public class CustomerApiTest {
	@Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

	@Test
	public void test() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
		stubFor(post(urlEqualTo("/v1/customers")).withHeader("Accept", equalTo("application/xml"))
				.willReturn(
	            aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "application/xml")
	                .withBody(
	                    TestConstants.XML_HEADER 
	                            + "<Customer>"
	                            + "<id>1</id>"
	                            + "<firstname>John</firstname>"
	                            + "<lasttname>Doe</lasttname>" 
	                            + "<street>Backer street 3</street>"
	                            + "<city>London</city>"
	                            + "</Customer>")));
	
		CustomerApi customer = new CustomerApi();
		customer.setCustomerFirstName("John");
		customer.setCustomerLastName("Doe");
		customer.setCustomerStreet("Backer street 3");
		customer.setCustomerCity("London");
		
		customer.createNewCustomer();
	}
}
