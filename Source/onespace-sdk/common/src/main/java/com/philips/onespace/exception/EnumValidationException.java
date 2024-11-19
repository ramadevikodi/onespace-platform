package com.philips.onespace.exception;


public class EnumValidationException extends RuntimeException {
    public EnumValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}