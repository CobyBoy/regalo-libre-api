package com.regalo_libre.wishlist.dto;

public record DashboardWishlistDto(
        String name,
        long totalGifts,
        Long id,
        String publicId,
        boolean isPrivate,
        String nickname
) {
}
