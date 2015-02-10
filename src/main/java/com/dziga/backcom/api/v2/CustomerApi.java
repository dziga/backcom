package com.dziga.backcom.api.v2;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.json.JSONException;

import com.dziga.backcom.domain.v2.Customer;
import com.dziga.backcom.domain.v2.ObjectFactory;
import com.dziga.backcom.rest.RestClient;

public class CustomerApi {
	
	private RestClient restClient;
	private Customer customer;
	private ObjectFactory objectFactory = new ObjectFactory();
	private String requestFormat = "xml";
	
	public CustomerApi() {
		restClient = new RestClient(RestEndpoints.HOST);
		restClient.addHeader("Content-Type", "application/xml, application/json");
		restClient.addHeader("Accept", "application/xml, application/json");
		customer = objectFactory.createCustomer();
	}
	
	public void setCustomerRequestFormat(String requestFormat) {
		this.requestFormat = requestFormat;
	}
	
	public void setCustomerId(long id) {
		customer.setId(id);
	}
	
	public void setCustomerFirstName(String name) {
		customer.setFirstName(name);
	}
	
	public void setCustomerLastName(String name) {
		customer.setLastName(name);
	}
	
	public void setCustomerSignedContractDate(String date) {
		customer.setSignedContractDate(date);
	}
	
	public void setCustomerStreet(String street) {
		customer.setStreet(street);
	}
	
	public void setCustomerStreetNumber(int number) {
		customer.setStreetNumber(number);
	}
	
	public void setCustomerCity(String city) {
		customer.setCity(city);
	}
	
	public void setCustomerPostalCode(Integer postCode) {
		customer.setPostalCode(BigInteger.valueOf(postCode));
	}
	
	public void getCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
		customer = (Customer) restClient.getFromService(objectFactory.createCustomer(customer), String.format(RestEndpoints.CUSTOMER, customer.getId()));
	}

	public void createNewCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
		restClient.setRequestType(requestFormat);
		customer = (Customer) restClient.postToService(objectFactory.createCustomer(customer), RestEndpoints.CUSTOMER_LIST);
	}

	public void editCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
		restClient.setRequestType(requestFormat);
		customer = (Customer) restClient.putToService(objectFactory.createCustomer(customer), String.format(RestEndpoints.CUSTOMER, customer.getId()));
	}
	
	public boolean deleteCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
		return restClient.deleteViaService(String.format(RestEndpoints.CUSTOMER, customer.getId()));
	}
	
	public long getCustomerId() {
		return customer.getId();
	}
	
	public String getCustomerFirstName() {
		return customer.getFirstName();
	}
	
	public String getCustomerLastName() {
		return customer.getLastName();
	}
	
	public String getCustomerSignedContractDate() {
		return customer.getSignedContractDate();
	}
	
	public String getCustomerStreet() {
		return customer.getStreet();
	}
	
	public int getCustomerStreetNumber() {
		return customer.getStreetNumber();
	}
	
	public String getCustomerCity() {
		return customer.getCity();
	}
	
	public Integer getCustomerPostalCode() {
		return (int) customer.getPostalCode().longValue();
	}
}
