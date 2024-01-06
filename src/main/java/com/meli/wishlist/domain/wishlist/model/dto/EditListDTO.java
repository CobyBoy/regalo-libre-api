package com.meli.wishlist.domain.wishlist.model.dto;

import com.meli.wishlist.domain.wishlist.model.WishListVisibility;

public record EditListDTO(
        String description,
        String name,
        WishListVisibility visibility
) {
}
