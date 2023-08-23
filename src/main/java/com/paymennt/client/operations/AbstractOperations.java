/************************************************************************
 * Copyright MODYM, Ltd.
 */
package com.paymennt.client.operations;

/**
 *
 * @author Ankur
 */
public class AbstractOperations {
    protected final PaymenntApiTransport client;

    public AbstractOperations(PaymenntApiTransport client) {
        this.client = client;
    }
}
