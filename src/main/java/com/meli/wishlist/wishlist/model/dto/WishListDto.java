package com.meli.wishlist.wishlist.model.dto;

import com.meli.wishlist.mercadolibre.auth.meli.model.MercadoLibreUser;
import com.meli.wishlist.mercadolibre.model.BookmarkedProduct;
import com.meli.wishlist.wishlist.model.WishList;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record WishListDto(
        Long id,
        String description,
        String name,
        LocalDateTime createdAt,
        String publicId,
        UUID privateId,
        /*@Enumerated(EnumType.STRING)
        WishListVisibility visibility,*/
        Boolean isPrivate,
        int totalGifts,
        MercadoLibreUser user,
        List<BookmarkedProduct> gifts) {

        public static WishListDto toDto(WishList wishList) {
                return WishListDto.builder()
                        .id(wishList.getId())
                        .createdAt(wishList.getCreatedAt())
                        .gifts(wishList.getGifts())
                        .description(wishList.getDescription())
                        .name(wishList.getName())
                        .publicId(wishList.getPublicId())
                        .privateId(wishList.getPrivateId())
                        .totalGifts(wishList.getTotalGifts())
                        //.visibility(wishList.getVisibility())
                        .isPrivate(wishList.getIsPrivate())
                        .user(wishList.getUser())
                        .build();
        }
}
