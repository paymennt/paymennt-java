/************************************************************************
 * Copyright PointCheckout Ltd.
 */

package com.paymennt.client.operations;

import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.model.Checkout;
import com.paymennt.client.request.WebCheckoutRequest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author Ankur
 */
public class CheckoutOperations extends AbstractOperations {

    public CheckoutOperations(PaymenntApiTransport client) {
        super(client);
    }

    public Checkout createWebCheckout(WebCheckoutRequest webCheckoutRequest) throws PaymenntClientException, URISyntaxException, IOException {
        webCheckoutRequest.validate();
        String path = "mer/v2.0/checkout/web";
        return this.client.doPost(path, null, webCheckoutRequest, null, Checkout.class);
    }

    public Checkout getCheckout(String checkoutId) throws PaymenntClientException, URISyntaxException, IOException {
        String path = "mer/v2.0/checkout/" + checkoutId;
        return this.client.doGet(path, null, null, Checkout.class);
    }
}
