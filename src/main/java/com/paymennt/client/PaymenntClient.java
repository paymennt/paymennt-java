/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client;

import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.operations.CheckoutOperations;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 *
 * @author Ankur
 */
public class PaymenntClient {

    private static final String API_KEY_HEADER = "X-Paymennt-Api-Key";
    private static final String API_SECRET_HEADER = "X-Paymennt-Api-Secret";
    private static final String DEFAULT_PREFIX = "api/mer/v2.0";
    private final HttpClient httpClient;


    private final CheckoutOperations checkoutOperations;

    /*******************************************************************************************************************
     * CONSTRUCTOR AND CONNECTION MANAGEMENT
     */
    public PaymenntClient(String apiKey, String apiSecret)
            throws PaymenntClientException {
        this(apiKey, apiSecret, PaymenntEnvironment.LIVE);
    }

    public PaymenntClient(String apiKey, String apiSecret, PaymenntEnvironment environment) throws PaymenntClientException {
        assertTrue(StringUtils.isNotBlank(apiKey), "apiKey cannot be empty");
        assertTrue(StringUtils.isNotBlank(apiSecret), "apiSecret cannot be empty");

        // CREATE HTTP CLIENT
        this.httpClient = HttpClientBuilder.create().setDefaultHeaders(
                List.of(
                        new BasicHeader(API_KEY_HEADER, apiKey),
                        new BasicHeader(API_SECRET_HEADER, apiSecret)
                )
        ).build();

        String scheme = environment.scheme;
        String host = environment.host;
        int port = environment.port;

        URIBuilder builder = new URIBuilder().setScheme(scheme).setHost(host).setPort(port)
                .setPath(DEFAULT_PREFIX);

        URI baseUri = null;
        try {
            baseUri = builder.build();
        } catch (URISyntaxException e) {
            throw new PaymenntClientException("Invalid URI: %s://%s:%d", scheme, host, port);
        }

        this.checkoutOperations = new CheckoutOperations(httpClient, baseUri);
    }

    /**
     * @return the checkoutOperations
     */
    public CheckoutOperations checkoutOperations() {
        return this.checkoutOperations;
    }

    private void assertTrue(boolean condition, String message) throws PaymenntClientException {
        if (!condition)
            throw new PaymenntClientException(message);
    }

    public enum PaymenntEnvironment {
        LIVE("https","api.paymennt.com", 443),
        TEST("https","api.test.paymennt.com", 443),
        LOCAL("http","localhost", 8080);

        private final String scheme;
        private final String host;
        private final int port;

        PaymenntEnvironment(String scheme, String host, int port) {
            this.scheme = scheme;
            this.host = host;
            this.port = port;
        }
    }

}
