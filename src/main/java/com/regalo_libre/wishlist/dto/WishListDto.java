package com.regalo_libre.wishlist.dto;

import com.regalo_libre.auth.OAuthPublicUserDTO;
import com.regalo_libre.utils.DtoConverter;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.wishlist.model.WishList;
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
        LocalDateTime updatedAt,
        String publicId,
        UUID privateId,

        Boolean isPrivate,
        int totalGifts,
        OAuthPublicUserDTO user,
        List<BookmarkedProduct> gifts) implements DtoConverter<WishList, WishListDto> {

        public WishListDto toDto(WishList wishList) {
                return WishListDto.builder()
                        .id(wishList.getId())
                        .createdAt(wishList.getCreatedAt())
                        .gifts(wishList.getGifts())
                        .description(wishList.getDescription())
                        .name(wishList.getName())
                        .publicId(wishList.getPublicId())
                        .privateId(wishList.getPrivateId())
                        .totalGifts(wishList.getTotalGifts())
                        .isPrivate(wishList.getIsPrivate())
                        .user(OAuthPublicUserDTO.builder().build().toDto(wishList.getUser()))
                        .updatedAt(wishList.getUpdatedAt())
                        .build();
        }
}
