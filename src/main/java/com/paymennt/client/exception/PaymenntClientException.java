/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client.exception;

import lombok.Getter;

import java.io.Serial;

/**
 *
 * @author Ankur
 */
public class PaymenntClientException extends Exception {
    @Serial
    private static final long serialVersionUID = 326864452189922315L;

    @Getter
    private final int responseCode;

    public PaymenntClientException(String message) {
        this(-1, message);
    }

    public PaymenntClientException(int responseCode, String message) {
        super(message);
        this.responseCode= responseCode;
    }

    public PaymenntClientException(String message, Object... args) {
        this(-1,String.format(message, args));
    }

    public PaymenntClientException(String message, Throwable cause, Object... args) {
        this(-1, String.format(message, args), cause);
    }

    public PaymenntClientException(int responseCode, String message, Throwable cause, Object... args) {
        super(String.format(message, args), cause);
        this.responseCode= responseCode;
    }
}
