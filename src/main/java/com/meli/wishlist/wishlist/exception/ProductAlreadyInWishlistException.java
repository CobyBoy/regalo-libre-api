package com.meli.wishlist.wishlist.exception;

public class ProductAlreadyInWishlistException extends RuntimeException {
    public ProductAlreadyInWishlistException(String message) {
        super(message);
    }
}
