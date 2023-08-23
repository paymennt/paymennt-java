/************************************************************************
 * Copyright PointCheckout Ltd.
 */

package com.paymennt.client.operations;

import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.model.Checkout;
import com.paymennt.client.request.WebCheckoutRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class providing operations related to creating and retrieving checkouts.
 * This class extends AbstractOperations and utilizes HTTP requests.
 *
 * @author Ankur
 */

@Log4j2
public class CheckoutOperations extends AbstractOperations {

    /**
     * Constructor to initialize the CheckoutOperations class.
     *
     * @param httpClient The HttpClient for checkout operations.
     * @param baseUri The base URI for checkout operations.
     */
    public CheckoutOperations(HttpClient httpClient, URI baseUri) {
        super(httpClient, baseUri);
    }

    /**
     * Create a web checkout using the provided WebCheckoutRequest.
     *
     * @param webCheckoutRequest Contains checkout details required for creation.
     * @return The created Checkout object.
     * @throws PaymenntClientException Custom PaymenntClient exception.
     * @throws URISyntaxException Exception while creating URI.
     * @throws IOException In case of a problem or connection abortion.
     */
    public Checkout createWebCheckout(WebCheckoutRequest webCheckoutRequest) throws PaymenntClientException, URISyntaxException, IOException {
        webCheckoutRequest.validate();
        String path = "checkout/web";
        log.info("Creating web checkout with request: {}", webCheckoutRequest);
        return this.doPost(path, null, webCheckoutRequest, Checkout.class);
    }

    /**
     * Retrieve a checkout using the provided checkout ID.
     *
     * @param checkoutId The Paymennt checkout reference ID.
     * @return The retrieved Checkout object.
     * @throws PaymenntClientException Custom PaymenntClient exception.
     * @throws URISyntaxException Exception while creating URI.
     * @throws IOException In case of a problem or connection abortion.
     */
    public Checkout getCheckout(String checkoutId) throws PaymenntClientException, URISyntaxException, IOException {
        String path = "checkout/" + checkoutId;
        log.info("Retrieving checkout with ID: {}", checkoutId);
        return this.doGet(path, null, Checkout.class);
    }
}
