package com.regalo_libre.wishlist.dto;

import com.regalo_libre.bookmarks.dto.BookmarkDetailsDto;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.utils.DtoConverter;
import com.regalo_libre.wishlist.model.WishList;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record WishListDetailDto(
        Long id,
        String description,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String publicId,
        UUID privateId,
        Boolean isPrivate,
        int totalGifts,
        String user,
        Page<BookmarkDetailsDto> gifts) {

    public WishListDetailDto toDto(WishList wishList, Page<BookmarkDetailsDto> gifts) {
        return WishListDetailDto.builder()
                .id(wishList.getWishlistId())
                .createdAt(wishList.getCreatedAt())
                .gifts(gifts)
                .description(wishList.getDescription())
                .name(wishList.getName())
                .publicId(wishList.getPublicId())
                .privateId(wishList.getPrivateId())
                .totalGifts(wishList.getTotalGifts())
                .isPrivate(wishList.getIsPrivate())
                .user(wishList.getUser().getProfile().getAppNickname())
                .updatedAt(wishList.getUpdatedAt())
                .build();
    }
}
