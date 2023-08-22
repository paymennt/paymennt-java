/************************************************************************
 * Copyright MODYM, Ltd.
 */
package com.paymennt.client.operations;

import com.paymennt.client.PaymenntClient;

/**
 *
 * @author Ankur
 */
public class AbstractOperations {
    protected final PaymenntClient client;

    public AbstractOperations(PaymenntClient client) {
        this.client = client;
    }
}
