package com.regalo_libre.wishlist.model.dto;

import lombok.Builder;

@Builder
public record WishListCreateRequestDto(
        String description,
        String name,
        Boolean isPrivate
) {
}
