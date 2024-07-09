package com.regalo_libre.profile;

public class ProfileNotPublicException extends RuntimeException {
    public ProfileNotPublicException(String message) {
        super(message);
    }
}