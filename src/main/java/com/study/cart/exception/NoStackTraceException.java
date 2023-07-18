package com.study.cart.exception;

public class NoStackTraceException extends RuntimeException{
    public NoStackTraceException(final String message) {
        this(message, null);
    }

    public NoStackTraceException(final String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
