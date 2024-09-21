package com.regalo_libre.wishlist.dto;

public record PublicProfileWishlistDto(
        Long id,
        String name,
        String publicId,
        long totalGifts
) {
}
