package com.paymennt.client.operations;

import com.paymennt.client.PaymenntClient;
import com.paymennt.client.exception.PaymenntClientException;
import com.paymennt.client.model.Checkout;
import com.paymennt.client.request.WebCheckoutRequest;

public class CheckoutOperations extends AbstractOperations {

    public CheckoutOperations(PaymenntClient client) {
        super(client);
    }

    public Checkout createWebCheckout(WebCheckoutRequest webCheckoutRequest) throws Exception {
        webCheckoutRequest.validate();
        String path = "checkout/web";
        return this.client.doPost(path, null, webCheckoutRequest, null, Checkout.class);
    }

    public Checkout getCheckout(String checkoutId) throws PaymenntClientException {
        String path = "checkout/" + checkoutId;
        return this.client.doGet(path, null, null, Checkout.class);
    }
}
