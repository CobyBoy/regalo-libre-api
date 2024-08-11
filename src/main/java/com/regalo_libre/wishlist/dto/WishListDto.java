package com.regalo_libre.wishlist.dto;

import com.regalo_libre.wishlist.model.WishList;

import java.time.LocalDateTime;
import java.util.UUID;
public record WishListDto(
        Long id,
        String description,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String publicId,
        UUID privateId,
        Boolean isPrivate,
        int totalGifts,
        String user) {
    public WishListDto(WishList wishList) {
        this(wishList.getWishlistId(),
                wishList.getDescription(),
                wishList.getName(),
                wishList.getCreatedAt(),
                wishList.getUpdatedAt(),
                wishList.getPublicId(),
                wishList.getPrivateId(),
                wishList.getIsPrivate(),
                wishList.getTotalGifts(),
                wishList.getUser().getProfile().getAppNickname());
    }
}
