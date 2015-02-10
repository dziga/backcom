# Backcom

Backcom is a template/example java project for testing REST interfaces built upon xsd schemas. It's task is to rapidly provide domain and adapt to domain changes of the service, translate java bean object to a message format (xml, json), so that user can be focused on writing tests in java.

## Motivation

Motivation for providing this template and with it this test approach comes from testing REST interfaces in large software landscape and rapid changes triggered by business requirements. Multiple teams working on one or more services base their collaboration on a "contract" (xsd schema and API). Ensuring that "contract" terms are met tester should have fast way to adapt to domain and API changes while ensuring backwards compatibility of API.

## Testing requirements

 - All service API changes should be followed with tests
 - Tests should ensure backwards compatibility
 - Tester should not waste time on writing and maintaining messages in xml and/or json format
 - Following service API version should be done in easy and fast manner
 - Can be used with existing test frameworks (jBehave, fitnesse...)

## Approach

All steps described bellow are already provided in this template/example. More details of the approach will be provided in the examples further on.

 - Get xsd schema in question from service and place it in the project
 - Use gradle or maven jaxb/wsdl plugin to generate the java model
 - Write a class that can be used by tests to instantiate the object of the provided model, fill it in with data and send to service as well as getting data retrieved from server (here we will call it test API)
 - For sending/retreiving data from server use ```RestClient.java``` class provided in example in package ```com.dziga.backcom.rest``` (this class will marshal java objects to xml/json and unmarshal xml/json back to java object)
 - Use retrieved objects in your tests to do further verification
 - In case of version two of service, add xsd version two, generate domain v2 in different package, copy test api package of version one and adapt it to new version

### XSD schema

``` src/main/resources/v1.xsd ```


```xml 
<?xml version="1.0" encoding="UTF-8" ?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">

	<xs:element name="CustomerList" type="CustomerList" />
	<xs:element name="Customer" type="Customer" />

	<xs:complexType name="CustomerList">
    		<xs:sequence>
      			<xs:element ref="Customer"/>
      		</xs:sequence>
    </xs:complexType>
    
    <xs:complexType name="Customer">
    		<xs:sequence>
    			<xs:element name="id" type="xs:long"/>
      			<xs:element name="firstname" type="xs:string"/>
      			<xs:element name="lastname" type="xs:string"/>
      			<xs:element name="street" type="xs:string"/>
      			<xs:element name="city" type="xs:string"/>
      		</xs:sequence>
     </xs:complexType>
</xs:schema>
```

### Gradle build

Execute model generating with ``` gradlew build ```

```gradle
apply plugin: 'java'

buildscript{
    repositories{
        jcenter() 
        mavenCentral()
    }
    dependencies {
        classpath 'no.nils:wsdl2java:0.6'
    }
}

allprojects { 
	apply plugin :'no.nils.wsdl2java'

	repositories{
    	mavenCentral()
	}
	dependencies {
		compile 'org.apache.httpcomponents:httpclient:4.3.6'
		compile 'commons-io:commons-io:2.4'
		compile 'org.apache.commons:commons-lang3:3.3.2'
		compile 'org.slf4j:slf4j-api:1.7.10'
		compile 'org.slf4j:slf4j-simple:1.7.10'
		compile 'org.json:json:20140107'
		testCompile 'junit:junit:4.11'
		testCompile 'com.github.tomakehurst:wiremock:1.53'
	}
}

xsd2java{
    xsdsToGenerate = [
        ["$projectDir/src/main/resources/xsd/v1.xsd", "com.dziga.backcom.domain.v1"],
        ["$projectDir/src/main/resources/xsd/v2.xsd", "com.dziga.backcom.domain.v2"]
    ]
    generatedXsdDir = file("$projectDir/src/generated/java")
}

sourceSets{
    main.java.srcDirs +=[wsdl2java.generatedWsdlDir]
}
```

### Test API class v1 for given 'Customer' domain

Create test API, instaintiate customer object from provided domain and initiate rest client.

```java
public class CustomerApi {
	
	private RestClient restClient;
	private Customer customer;
	private ObjectFactory objectFactory = new ObjectFactory();
	
	public CustomerApi() {
		restClient = new RestClient(RestEndpoints.HOST);
		restClient.addHeader("Content-Type", "application/xml");
		restClient.addHeader("Accept", "application/xml");
		//object factory is auto-generated with gradle plugin
		customer = objectFactory.createCustomer();
	}
	```
	
	Provide methods for filling in objects with data and retreiving data from object after service call has ended.
	
	```java
	public void setCustomerId(long id) {
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
	
	
	public long getCustomerId() {
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
```

Provide methods for sending messages to service using rest client provided in the template/example. Notice the usage of object factory for creating customer from instantiated and filled in object. 

_RestEndpoint_ is class filled in with paths of resources on service.

```java
public void getCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
		customer = (Customer) restClient.getFromService(objectFactory.createCustomer(customer), String.format(RestEndpoints.CUSTOMER, customer.getId()));
}
	
public void createNewCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
		customer = (Customer) restClient.postToService(objectFactory.createCustomer(customer), RestEndpoints.CUSTOMER_LIST);
}
	
public void editCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
		customer = (Customer) restClient.putToService(objectFactory.createCustomer(customer), String.format(RestEndpoints.CUSTOMER, customer.getId()));
}
	
public boolean deleteCustomer() throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
		return restClient.deleteViaService(String.format(RestEndpoints.CUSTOMER, customer.getId()));
}
```

### Test examples

Example of POST request for creating new customer. Assuming that service will give saved object as the response.

```java
  CustomerApi customer = new CustomerApi();
	customer.setCustomerFirstName("John");
	customer.setCustomerLastName("Doe");
	customer.setCustomerStreet("Backer street 3");
	customer.setCustomerCity("London");
		
	customer.createNewCustomer();
	
	Assert.assertEquals(1, customer.getCustomerId());	
	Assert.assertEquals("John", customer.getCustomerFirstName());
	Assert.assertEquals("Doe", customer.getCustomerLastName());
	Assert.assertEquals("Backer street 3", customer.getCustomerStreet());
	Assert.assertEquals("London", customer.getCustomerCity());
```

Example of GET request for checking the details of the customer. Assuming that customer with id 1 exists in the service.

```java
  CustomerApi customer = new CustomerApi();
	customer.setCustomerId(1);
		
	customer.getCustomer();
		
	Assert.assertEquals(1, customer.getCustomerId());
	Assert.assertEquals("John", customer.getCustomerFirstName());
	Assert.assertEquals("Doe", customer.getCustomerLastName());
	Assert.assertEquals("Backer street 3", customer.getCustomerStreet());
	Assert.assertEquals("London", customer.getCustomerCity());
```
