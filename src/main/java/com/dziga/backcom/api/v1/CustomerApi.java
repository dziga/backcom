package com.dziga.backcom.api.v1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.dziga.backcom.domain.tb.Customer;
import com.dziga.backcom.domain.tb.ObjectFactory;
import com.dziga.backcom.rest.RestClient;

public class CustomerApi {
	
	private RestClient restClient;
	private Customer customer;
	private ObjectFactory objectFactory = new ObjectFactory();
	
	public CustomerApi() {
		restClient = new RestClient(RestEndpoints.HOST);
		restClient.addHeader("Content-Type", "application/xml");
		restClient.addHeader("Accept", "application/xml");
		customer = objectFactory.createCustomer();
	}
	
	public void setCustomerId(Long id) {
		customer.setId(id);
	}
	
	public void setCustomerFirstName(String name) {
		customer.setFirstname(name);
	}
	
	public void setCustomerLastName(String name) {
		customer.setLastname(name);
	}
	
	public void setCustomerStreet(String street) {
		customer.setStreet(street);
	}
	
	public void setCustomerCity(String city) {
		customer.setCity(city);
	}
	
	public void createNewCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
		customer = (Customer) restClient.postToService(objectFactory.createCustomer(customer), RestEndpoints.CUSTOMER_LIST);
	}
	
	public Long getCustomerId() {
		return customer.getId();
	}
	
	public String getCustomerFirstName() {
		return customer.getFirstname();
	}
	
	public String getCustomerLastName() {
		return customer.getLastname();
	}
	
	public String getCustomerStreet() {
		return customer.getStreet();
	}
	
	public String getCustomerCity() {
		return customer.getCity();
	}
}
