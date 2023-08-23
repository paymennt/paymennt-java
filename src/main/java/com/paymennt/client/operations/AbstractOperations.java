/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client.operations;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.response.ApiResponse;
import com.paymennt.client.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
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
import java.util.Objects;

/**
 * Abstract class providing common operations for making HTTP requests.
 * This class is designed to be extended for specific operations.
 *
 * @author Ankur
 */

@Log4j2
public abstract class AbstractOperations {

    private final HttpClient httpClient;
    private final URI baseUri;

    /**
     * Constructor to initialize the AbstractOperations class.
     *
     * @param httpClient - The HttpClient to be used for making requests.
     * @param baseUri - The base URI for the operation.
     */
    protected AbstractOperations(HttpClient httpClient, URI baseUri) {
        this.httpClient = httpClient;
        this.baseUri = baseUri;
    }

    /**
     * Perform an HTTP GET request.
     *
     * @param path - The path of the URL.
     * @param queryParameters - Query parameters to be added to the request.
     * @param toValueType - Class to which the response should be cast.
     * @param <T> - The generic type of the response.
     * @return The response object of type T.
     * @throws PaymenntClientException - Custom PaymenntClient exception.
     * @throws URISyntaxException - Exception while creating URI.
     * @throws IOException - In case of a problem or connection abortion.
     */
    protected <T> T doGet(
            String path,
            Map<String, Object> queryParameters,
            Class<T> toValueType) throws PaymenntClientException, URISyntaxException, IOException {

        URI uri = this.getURI(path, queryParameters);
        HttpGet httpGet = new HttpGet(uri);
        log.info("Performing GET request to: {}", uri);
        return this.execute(httpGet, toValueType);
    }

    /**
     * Perform an HTTP POST request.
     *
     * @param path - The path of the URL.
     * @param queryParameters - Query parameters to be added to the request.
     * @param postBody - Request body for the post request.
     * @param toValueType - Class to which the response should be cast.
     * @param <T> - The generic type of the response.
     * @return The response object of type T.
     * @throws PaymenntClientException - Custom PaymenntClient exception.
     * @throws URISyntaxException - Exception while creating URI.
     * @throws IOException - In case of a problem or connection abortion.
     */
    protected <T> T doPost(
            String path,
            Map<String, Object> queryParameters,
            Object postBody,
            Class<T> toValueType) throws PaymenntClientException, URISyntaxException, IOException {

        if (postBody != null && !JsonUtils.canEncodeDecode(postBody))
            throw new PaymenntClientException("Unable to serialize post body of type '%s'", postBody.getClass().getName());

        URI uri = this.getURI(path, queryParameters);
        HttpPost httpPost = new HttpPost(uri);
        log.info("Performing POST request to: {}", uri);
        if (postBody != null) {
            HttpEntity entity = new StringEntity(JsonUtils.encode(postBody), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
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
        if (!Objects.isNull(apiResponse.getError())) {
            log.error("Request failed with error: {}", apiResponse.getError());
            throw new PaymenntClientException(apiResponse.getError());
        }

        log.info("Request successful.");
        return apiResponse.getResult();
    }
}
