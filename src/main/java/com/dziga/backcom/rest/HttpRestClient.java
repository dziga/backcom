package com.dziga.backcom.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class HttpRestClient {
	private HttpClient httpClient = HttpClientBuilder.create().build();
	private List<Header> headers = new ArrayList<Header>();;
	private Map<String, String> queryParams = new HashMap<String, String>();
	private String scheme = "http";
	private String host;
	private String path;
	private String responseBody;
	private int responseStatusCode;

	
	
	public HttpRestClient(String scheme, String host, String path) {
		this.scheme = scheme;
		this.host = host;
		this.path = path;
	}
	
	public HttpRestClient(String host, String path) {
		this.host = host;
		this.path = path;
	}
	
	public HttpRestClient(String host) {
		this.host = host;
	}
	
	public void setRequestPath(String path) {
		this.path = path;
	}

	public int Get() throws IOException, URISyntaxException,
			NoSuchAlgorithmException, InvalidKeyException {
		URIBuilder b = new URIBuilder().setScheme(scheme).setHost(host).setPath(path);
		addQueryParams(b);
		URI fullUri = b.build();
		HttpGet getMethod = new HttpGet(fullUri);
		addHeadersToMethod(getMethod);

		processResponse(httpClient.execute(getMethod));
		return responseStatusCode;
	}

	public int Post(String body) throws URISyntaxException, ClientProtocolException, IOException {
		URIBuilder b = new URIBuilder().setScheme(scheme).setHost(host).setPath(path);
        addQueryParams(b);
        URI fullUri = b.build();
        HttpPost postMethod = new HttpPost(fullUri);
        HttpEntity entity = new StringEntity(body);
        postMethod.setEntity(entity);
        addHeadersToMethod(postMethod);

        processResponse(httpClient.execute(postMethod));

        return responseStatusCode;
	}

	public int Put(String body) throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder b = new URIBuilder().setScheme(scheme).setHost(host).setPath(path);
        addQueryParams(b);
        URI fullUri = b.build();
        HttpPut putMethod = new HttpPut(fullUri);
        HttpEntity entity = new StringEntity(body);
        putMethod.setEntity(entity);
        addHeadersToMethod(putMethod);

        processResponse(httpClient.execute(putMethod));

        return responseStatusCode;
	}

	public int Delete() throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder().setScheme(scheme).setHost(host).setPath(path);
		addQueryParams(uriBuilder);
		URI fullUri = uriBuilder.build();
		HttpDelete deleteMethod = new HttpDelete(fullUri);
		addHeadersToMethod(deleteMethod);

		processResponse(httpClient.execute(deleteMethod));

		return responseStatusCode;
	}

	public void addHeader(String headerName, String headerValue) {
		Header foundHeader = null;
		for (Header header : headers) {
			if (StringUtils.equalsIgnoreCase(header.getName(), headerName)) {
				foundHeader = header;
				break;
			}
		}
		if (foundHeader != null) {
			headers.remove(foundHeader);
		}
		headers.add(new BasicHeader(headerName, headerValue));
	}
	
	public void addQueryParameter(String key, String value) {
		queryParams.put(key, value);
	}
	
	public void removeAllQueryParameters() {
		queryParams.clear();
	}

	public String getResponseBody() {
		return responseBody;
	}

	private void addHeadersToMethod(HttpRequestBase method) {
		for (Header h : headers) {
			method.addHeader(h.getName(), h.getValue());
		}
	}

	private void addQueryParams(URIBuilder b) {
		for (Map.Entry<String, String> param : queryParams.entrySet()) {
			b.addParameter(param.getKey(), param.getValue());
		}
	}
	
	private void processResponse(HttpResponse response) throws IOException {
        responseStatusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        responseBody = EntityUtils.toString(entity);
    }

}
