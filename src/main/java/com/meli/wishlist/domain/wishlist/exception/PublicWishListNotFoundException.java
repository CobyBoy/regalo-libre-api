package com.meli.wishlist.domain.wishlist.exception;

public class PublicWishListNotFoundException extends RuntimeException {
    public PublicWishListNotFoundException(String message) {
        super(message);
    }
}