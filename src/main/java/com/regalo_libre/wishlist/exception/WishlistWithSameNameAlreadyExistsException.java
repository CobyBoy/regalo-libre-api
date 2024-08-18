package com.regalo_libre.wishlist.exception;

public class WishlistWithSameNameAlreadyExistsException extends RuntimeException {
    public WishlistWithSameNameAlreadyExistsException(String message) {
        super(message);
    }
}
