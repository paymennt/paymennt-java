package com.paymennt.client.exception;

import lombok.Getter;

public class PaymenntClientException extends Exception {
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

    public PaymenntClientException(int responseCode, String message, Object... args) {
        super(String.format(message, args));
        this.responseCode= responseCode;
    }

    public PaymenntClientException(String message, Throwable cause, Object... args) {
        this(-1, String.format(message, args), cause);
    }

    public PaymenntClientException(int responseCode, String message, Throwable cause, Object... args) {
        super(String.format(message, args), cause);
        this.responseCode= responseCode;
    }
}
