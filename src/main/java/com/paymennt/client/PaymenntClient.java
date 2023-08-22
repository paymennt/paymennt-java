/************************************************************************
 * Copyright PointCheckout Ltd.
 */

package com.paymennt.client;

import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.model.PaymenntModel;
import com.paymennt.client.response.ApiResponse;
import com.paymennt.client.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ankur
 */

public class PaymenntClient {

    private final HttpClient httpClient = new DefaultHttpClient(new PoolingClientConnectionManager());
    private static final String API_KEY_HEADER = "X-Paymennt-Api-Key";
    private static final String API_SECRET_HEADER = "X-Paymennt-Api-Secret";

    private final String apiKey;
    private final String apiSecret;
    private final String publicKey;
    private final PaymenntClient.PaymenntEnvironment env;

    public PaymenntClient(String apiKey, String apiSecret) throws PaymenntClientException {
        this(apiKey, apiSecret, PaymenntClient.PaymenntEnvironment.LIVE, null);
    }

    public PaymenntClient(String apiKey, String apiSecret, PaymenntClient.PaymenntEnvironment env, String publicKey) throws PaymenntClientException {
        if (StringUtils.isBlank(apiKey))
            throw new PaymenntClientException("Unable to initiate PaymenntClient: Missing apiKey");
        if (StringUtils.isBlank(apiSecret))
            throw new PaymenntClientException("Unable to initiate PaymenntClient: Missing apiSecret");
        if (env == null)
            throw new PaymenntClientException("Unable to initiate PaymenntClient: Invalid environment");

        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.publicKey = publicKey;
        this.env = env;
    }


    public <T extends PaymenntModel> T doGet(
            String path,
            Map<String, Object> queryParameters,
            Map<String, String> headers,
            Class<T> cast) throws PaymenntClientException {

        headers = this.getDefaultHeaderMap(headers);
        try {
            URI uri = this.getURI(path, queryParameters);
            HttpGet get = new HttpGet(uri);
            this.setHeaders(get, headers);
            return this.execute(get, cast);
        } catch (Exception e) {
            throw new PaymenntClientException(e.getMessage(),e);
        }
    }

    public <T extends PaymenntModel> T doPost(
            String path,
            Map<String, Object> queryParameters,
            Object postBody,
            Map<String, String> headers,
            Class<T> cast) throws PaymenntClientException {

        if (postBody != null && !JsonUtils.canEncodeDecode(postBody))
            throw new PaymenntClientException("Unable to serialize post body of type '%s'", postBody.getClass().getName());

        try {
            URI uri = this.getURI(path, queryParameters);
            HttpPost post = new HttpPost(uri);
            headers = this.getDefaultHeaderMap(headers);
            if (postBody != null) {
                HttpEntity entity = new StringEntity(JsonUtils.encode(postBody), ContentType.APPLICATION_JSON);
                post.setEntity(entity);
                headers.put("Content-type", "application/json");
            }
            this.setHeaders(post, headers);
            return this.execute(post, cast);
        } catch (Exception e) {
            throw new PaymenntClientException(e.getMessage(), e);
        }
    }

    private Map<String, String> getDefaultHeaderMap(Map<String, String> map) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json; charset=UTF-8");
        headerMap.put(API_KEY_HEADER, apiKey);
        headerMap.put(API_SECRET_HEADER, apiSecret);
        if (map != null && !map.isEmpty())
            headerMap.putAll(map);
        return headerMap;
    }

    private void setHeaders(HttpMessage message, Map<String, String> headers) {
        if (headers == null)
            return;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            message.setHeader(entry.getKey(), entry.getValue());
        }
    }

    private URI getURI(String path, Map<String, Object> parameters) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder = uriBuilder.setPath(env.getBaseUrl()+ "/" + path);

        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue() != null ? entry.getValue().toString() : null;
                uriBuilder.setParameter(key, value);
            }
        }
        return uriBuilder.build();
    }

    private <T extends PaymenntModel> T execute(HttpUriRequest request, Class<T> cast)
            throws IOException,
            PaymenntClientException {
        HttpResponse response = this.httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        ApiResponse apiResponse = JsonUtils.decode(result, ApiResponse.class);
        EntityUtils.consume(entity);

        return JsonUtils.decode(apiResponse.getResult(), cast);
    }

    public enum PaymenntEnvironment {
        LIVE("https://api.paymennt.com/mer/v2.0"),
        TEST("https://api.test.paymennt.com/mer/v2.0"),
        LOCAL("http://localhost:8080/api/mer/v2.0");

        private final String baseUrl;

        PaymenntEnvironment(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getBaseUrl() {
            return baseUrl;
        }
    }


}
