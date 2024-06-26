package com.meli.wishlist.domain.wishlist.model.dto;

import lombok.Builder;

@Builder
public record WishListCreateRequestDto(
        String description,
        String name,
        Boolean isPrivate
) {
}
