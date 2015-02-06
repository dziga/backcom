package com.dziga.backcom.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RestClient {
    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    private HttpRestClient rest;
    private int responseCode;
    
    public RestClient(String host) {
    	rest = new HttpRestClient(host);
    }
    
    public RestClient(String scheme, String host) {
    	rest = new HttpRestClient(scheme, host);
    }
    
	private enum RequestType { 
        GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");
           
        private String requestType;
 
        private RequestType(String s) {
            requestType = s;
        }
       
        public String getRequestType() {
          return requestType;
        }
    };
    
    public int getResponseCode () {
        return responseCode;
    }
    
    public String getResponseBody () {
        return rest.getResponseBody();
    }
    
    public Object getFromService(Object returningObject, String servicePath, HashMap<String, String> params) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException {
        return doRequest(RequestType.GET, null, returningObject, servicePath, params);
    }
    
    public Object getFromService(Object returningObject, String servicePath) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException {
        return getFromService(returningObject, servicePath, null);
    }
    
    public Object getFromService(String servicePath) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException {
        return getFromService(null, servicePath, null);
    }
    
    public Object postToService(Object abstractObject, Object returningObject, String servicePath, HashMap<String, String> params) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException {
        return doRequest(RequestType.POST, abstractObject, returningObject, servicePath, params);
    }
    
    public Object postToService(Object abstractObject, String servicePath, HashMap<String, String> params) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
        return postToService(abstractObject, null, servicePath, params);
    }
    
    public Object postToService(Object abstractObject, Object returningObject, String servicePath) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
        return postToService(abstractObject, returningObject, servicePath, null);
    }
    
    public Object postToService(Object abstractObject, String servicePath) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
        return postToService(abstractObject, null, servicePath, null);
    }

    public Object putToService(Object abstractObject, Object returningObject, String servicePath, HashMap<String, String> params) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException {
        return doRequest(RequestType.PUT, abstractObject, returningObject, servicePath, params);
    }
    
    public Object putToService(Object abstractObject, String servicePath, HashMap<String, String> params) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
        return putToService(abstractObject, null, servicePath, params);
    }
    
    public Object putToService(Object abstractObject, Object returningObject, String servicePath) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
        return putToService(abstractObject, returningObject, servicePath, null);
    }
    
    public Object putToService(Object abstractObject, String servicePath) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException {
        return putToService(abstractObject, null, servicePath, null);
    }
    
    public boolean deleteViaService(String servicePath) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException {
        doRequest(RequestType.DELETE, null, null, servicePath, null);
        return responseCode == 200 || responseCode == 204;
    }

    private Object doRequest(RequestType requestType, Object modelObject, Object returningModelObject, String servicePath, HashMap<String, String> params) throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, KeyManagementException, XMLStreamException {
        if (params != null && !(params.isEmpty())) {
            for (Entry<String, String> param : params.entrySet()) {
            	rest.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        rest.setRequestPath(servicePath);
        if (requestType.getRequestType().equals("GET")) {
        	responseCode = rest.Get();
            if (returningModelObject == null) {
                return new Object();
            }
        }
        else if (requestType.getRequestType().equals("POST")) {
            responseCode = rest.Post((String) Marshal.toXml(modelObject));
        }
        else if (requestType.getRequestType().equals("PUT")) {
            responseCode = rest.Put((String) Marshal.toXml(modelObject));
        }
        else if (requestType.getRequestType().equals("DELETE")) {
            responseCode = rest.Delete();
            return new Object();
        }
        else {
            throw new IllegalStateException("Not known request type: " + requestType.getRequestType());
        }
        LOG.debug("Response body '{}'", rest.getResponseBody());
        rest.removeAllQueryParameters();
        if (returningModelObject == null) {
            returningModelObject = modelObject;
        }
        return (Object) Marshal.toObject(rest.getResponseBody(), returningModelObject);
    }
}
