package com.regalo_libre.wishlist.dto;

import com.regalo_libre.wishlist.model.WishList;

public record WishlistDetailForModalDto(
        Long id,
        String name,
        int totalGifts
) {

    public WishlistDetailForModalDto(WishList wishList) {
        this(wishList.getWishlistId(), wishList.getName(), wishList.getTotalGifts());
    }
}
