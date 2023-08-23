/************************************************************************
 * Copyright PointCheckout Ltd.
 */

package com.paymennt.client.operations;

import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.model.Checkout;
import com.paymennt.client.request.WebCheckoutRequest;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Ankur
 */
public class CheckoutOperations extends AbstractOperations {

    public CheckoutOperations(HttpClient httpClient, URI baseUri) {
        super(httpClient, baseUri);
    }

    public Checkout createWebCheckout(WebCheckoutRequest webCheckoutRequest) throws PaymenntClientException, URISyntaxException, IOException {
        webCheckoutRequest.validate();
        String path = "checkout/web";
        return this.doPost(path, null, webCheckoutRequest, Checkout.class);
    }

    public Checkout getCheckout(String checkoutId) throws PaymenntClientException, URISyntaxException, IOException {
        String path = "checkout/" + checkoutId;
        return this.doGet(path, null, Checkout.class);
    }
}
