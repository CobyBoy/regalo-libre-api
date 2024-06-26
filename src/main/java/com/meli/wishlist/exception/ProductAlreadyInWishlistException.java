package com.meli.wishlist.exception;

public class ProductAlreadyInWishlistException extends RuntimeException {
    public ProductAlreadyInWishlistException(String message) {
        super(message);
    }
}
