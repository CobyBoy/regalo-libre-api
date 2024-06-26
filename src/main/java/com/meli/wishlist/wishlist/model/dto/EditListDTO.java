package com.meli.wishlist.wishlist.model.dto;

public record EditListDTO(
        String description,
        String name,
        /*@Enumerated(EnumType.STRING)
        WishListVisibility visibility*/
        Boolean isPrivate
) {
}
