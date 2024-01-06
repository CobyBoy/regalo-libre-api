package com.meli.wishlist.domain.wishlist.model.dto;

import com.meli.wishlist.domain.mercadolibre.MercadoLibreProduct;
import com.meli.wishlist.domain.wishlist.model.WishList;
import com.meli.wishlist.domain.wishlist.model.WishListVisibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

import java.util.List;

@Builder
public record WishListDto(
        Long id,
        String description,
        String name,
        @Enumerated(EnumType.STRING)
        WishListVisibility visibility,
        List<MercadoLibreProduct> products) {

        public static WishListDto toDto(WishList wishList) {
                return WishListDto.builder()
                        .id(wishList.getId())
                        .products(wishList.getProducts())
                        .description(wishList.getDescription())
                        .name(wishList.getName())
                        .visibility(wishList.getVisibility())
                        .build();
        }
}
