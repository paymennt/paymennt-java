/************************************************************************
 * Copyright PointCheckout Ltd.
 */
package com.paymennt.client.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * Custom exception class for handling errors in the PaymenntClient.
 * This exception provides a response code and supports formatted messages.
 *
 * @author Ankur
 */
public class PaymenntClientException extends Exception {
    @Serial
    private static final long serialVersionUID = 326864452189922315L;

    /**
     * The HTTP response code associated with the exception.
     */
    @Getter
    private final int responseCode;

    /**
     * Constructs a PaymenntClientException with a given message.
     *
     * @param message The exception message.
     */
    public PaymenntClientException(String message) {
        this(-1, message);
    }

    /**
     * Constructs a PaymenntClientException with a given response code and message.
     *
     * @param responseCode The HTTP response code.
     * @param message The exception message.
     */
    public PaymenntClientException(int responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    /**
     * Constructs a PaymenntClientException with a formatted message.
     *
     * @param message The exception message format.
     * @param args Arguments to be formatted into the message.
     */
    public PaymenntClientException(String message, Object... args) {
        this(-1, String.format(message, args));
    }

    /**
     * Constructs a PaymenntClientException with a formatted message and cause.
     *
     * @param message The exception message format.
     * @param cause The cause of the exception.
     * @param args Arguments to be formatted into the message.
     */
    public PaymenntClientException(String message, Throwable cause, Object... args) {
        this(-1, String.format(message, args), cause);
    }

    /**
     * Constructs a PaymenntClientException with a given response code, formatted message, and cause.
     *
     * @param responseCode The HTTP response code.
     * @param message The exception message format.
     * @param cause The cause of the exception.
     * @param args Arguments to be formatted into the message.
     */
    public PaymenntClientException(int responseCode, String message, Throwable cause, Object... args) {
        super(String.format(message, args), cause);
        this.responseCode = responseCode;
    }
}
