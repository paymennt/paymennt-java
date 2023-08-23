/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client.operations;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.response.ApiResponse;
import com.paymennt.client.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 *
 * @author Ankur
 */
public abstract class AbstractOperations {

    private final HttpClient httpClient;
    private final URI baseUri;

    protected AbstractOperations(HttpClient httpClient, URI baseUri) {
        this.httpClient = httpClient;
        this.baseUri = baseUri;
    }

    protected <T> T doGet(
            String path,
            Map<String, Object> queryParameters,
            Class<T> toValueType) throws PaymenntClientException, URISyntaxException, IOException {

        URI uri = this.getURI(path, queryParameters);
        HttpGet httpGet = new HttpGet(uri);
        return this.execute(httpGet, toValueType);
    }

    protected <T> T doPost(
            String path,
            Map<String, Object> queryParameters,
            Object postBody,
            Class<T> toValueType) throws PaymenntClientException, URISyntaxException, IOException {

        if (postBody != null && !JsonUtils.canEncodeDecode(postBody))
            throw new PaymenntClientException("Unable to serialize post body of type '%s'", postBody.getClass().getName());

        URI uri = this.getURI(path, queryParameters);
        HttpPost httpPost = new HttpPost(uri);
        if (postBody != null) {
            HttpEntity entity = new StringEntity(JsonUtils.encode(postBody), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-type", "application/json");
        }
        return this.execute(httpPost, toValueType);
    }

    /*******************************************************************************************************************
     * PRIVATE METHOD CALLS
     */
    private URI getURI(String path, Map<String, Object> parameters) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(this.baseUri).setPath(this.baseUri.getPath() + "/" + path);
        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() != null ? entry.getValue().toString() : null;
                uriBuilder.setParameter(key, value);
            }
        }
        return uriBuilder.build();
    }

    private <T> T execute(HttpUriRequest request, Class<T> toValueType)
            throws IOException,
            PaymenntClientException {
        HttpResponse response = this.httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        TypeFactory typeFactory = JsonUtils.getObjectMapper().getTypeFactory();
        JavaType javaType = typeFactory.constructParametricType(ApiResponse.class, toValueType);
        ApiResponse<T> apiResponse = JsonUtils.decode(result, javaType);
        return apiResponse.getResult();
    }
}
