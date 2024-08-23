package com.regalo_libre.login.exception;

public class Auth0TokenIsUndefinedException extends RuntimeException {
    public Auth0TokenIsUndefinedException(String message) {
        super(message);
    }
}
