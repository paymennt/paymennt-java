/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client;

import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.operations.CheckoutOperations;
import com.paymennt.client.operations.PaymenntApiTransport;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Ankur
 */
public class PaymenntClient {
    private static final String DEFAULT_PREFIX = "mer/v2.0";

    private final CheckoutOperations checkoutOperations;

    /*******************************************************************************************************************
     * CONSTRUCTOR AND CONNECTION MANAGEMENT
     */
    public PaymenntClient(String clientKey, String clientSecret)
            throws PaymenntClientException {
        this(clientKey, clientSecret, PaymenntEnvironment.LIVE);
    }

    public PaymenntClient(String clientKey, String clientSecret, PaymenntEnvironment environment) throws PaymenntClientException {
        String scheme = environment.scheme;
        String host = environment.host;
        int port = environment.port;
        assertTrue(StringUtils.isNotBlank(clientKey), "clientKey cannot be empty");
        assertTrue(StringUtils.isNotBlank(clientSecret), "clientSecret cannot be empty");

        URIBuilder builder = new URIBuilder().setScheme(scheme).setHost(host).setPort(port)
                .setPath(DEFAULT_PREFIX);

        URI baseUri = null;
        try {
            baseUri = builder.build();
        } catch (URISyntaxException e) {
            throw new PaymenntClientException("Invalid URI: %s://%s:%d", scheme, host, port);
        }

        PaymenntApiTransport transport = new PaymenntApiTransport(clientKey, clientSecret, baseUri);
        this.checkoutOperations = new CheckoutOperations(transport);
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
        TEST("https","api.test.paymennt.com", 443);

        private final String scheme;
        private final String host;
        private final int port;

        PaymenntEnvironment(String scheme, String host, int port) {
            this.scheme = scheme;
            this.host = host;
            this.port = port;
        }

        public String getScheme() {
            return scheme;
        }
        public String getHost() {
            return host;
        }
        public int getPort() {
            return port;
        }
    }
}
