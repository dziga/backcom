package com.dziga.backcom.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.json.JSONException;

public class RestClient {

    private HttpRestClient rest;
    private int responseCode;
    private RequestFormat requestFormat = RequestFormat.xml;
    
    public RestClient(String host) {
    	rest = new HttpRestClient(host);
    }
    
    public RestClient(String scheme, String host) {
    	rest = new HttpRestClient(scheme, host);
    }
    
	private enum RequestFormat { 
        json("json"), xml("xml");
           
        private String requestFormat;
 
        private RequestFormat(String s) {
        	requestFormat = s;
        }
       
        public String getRequestFormat() {
          return requestFormat;
        }
    };
    
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
    
    public void setRequestType (String requestFormat) {
    	this.requestFormat = RequestFormat.valueOf(requestFormat);
    }
    
    public void addHeader(String headerName, String headerValue) {
		rest.addHeader(headerName, headerValue);
	}
    
    public int getResponseCode () {
        return responseCode;
    }
    
    public String getResponseBody () {
        return rest.getResponseBody();
    }
    
    public Object getFromService(Object returningObject, String servicePath, HashMap<String, String> params) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException, JSONException {
        return doRequest(RequestType.GET, null, returningObject, servicePath, params);
    }
    
    public Object getFromService(Object returningObject, String servicePath) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException, JSONException {
        return getFromService(returningObject, servicePath, null);
    }
    
    public Object postToService(Object abstractObject, Object returningObject, String servicePath, HashMap<String, String> params) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException, JSONException {
        return doRequest(RequestType.POST, abstractObject, returningObject, servicePath, params);
    }
    
    public Object postToService(Object abstractObject, String servicePath, HashMap<String, String> params) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
        return postToService(abstractObject, null, servicePath, params);
    }
    
    public Object postToService(Object abstractObject, Object returningObject, String servicePath) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
        return postToService(abstractObject, returningObject, servicePath, null);
    }
    
    public Object postToService(Object abstractObject, String servicePath) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
        return postToService(abstractObject, null, servicePath, null);
    }

    public Object putToService(Object abstractObject, Object returningObject, String servicePath, HashMap<String, String> params) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException, JSONException {
        return doRequest(RequestType.PUT, abstractObject, returningObject, servicePath, params);
    }
    
    public Object putToService(Object abstractObject, String servicePath, HashMap<String, String> params) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
        return putToService(abstractObject, null, servicePath, params);
    }
    
    public Object putToService(Object abstractObject, Object returningObject, String servicePath) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
        return putToService(abstractObject, returningObject, servicePath, null);
    }
    
    public Object putToService(Object abstractObject, String servicePath) throws KeyManagementException, InvalidKeyException, NoSuchAlgorithmException, JAXBException, URISyntaxException, IOException, XMLStreamException, JSONException {
        return putToService(abstractObject, null, servicePath, null);
    }
    
    public boolean deleteViaService(String servicePath) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, XMLStreamException, JSONException {
        doRequest(RequestType.DELETE, null, null, servicePath, null);
        return responseCode == 200 || responseCode == 204;
    }

    private Object doRequest(RequestType requestType, Object modelObject, Object returningModelObject, String servicePath, HashMap<String, String> params) throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException, IOException, JAXBException, KeyManagementException, XMLStreamException, JSONException {
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
        	if (requestFormat.getRequestFormat().equals("json")) {
        		responseCode = rest.Post((String) Marshal.toJson(modelObject));
        	}
        	else {
        		responseCode = rest.Post((String) Marshal.toXml(modelObject));
        	}
        }
        else if (requestType.getRequestType().equals("PUT")) {
        	if (requestFormat.getRequestFormat().equals("json")) {
        		responseCode = rest.Put((String) Marshal.toJson(modelObject));
        	}
        	else {
        		responseCode = rest.Put((String) Marshal.toXml(modelObject));
        	}
        }
        else if (requestType.getRequestType().equals("DELETE")) {
            responseCode = rest.Delete();
            return new Object();
        }
        else {
            throw new IllegalStateException("Not known request type: " + requestType.getRequestType());
        }
        rest.removeAllQueryParameters();
        if (returningModelObject == null) {
            returningModelObject = modelObject;
        }
        return (Object) Marshal.toObject(rest.getResponseBody(), returningModelObject);
    }
}
