package com.meli.wishlist.domain.wishlist.model.dto;

import com.meli.wishlist.domain.wishlist.model.WishListVisibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record EditListDTO(
        String description,
        String name,
        /*@Enumerated(EnumType.STRING)
        WishListVisibility visibility*/
        Boolean isPrivate
) {
}
