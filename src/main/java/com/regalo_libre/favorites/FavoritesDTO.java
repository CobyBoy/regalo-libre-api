package com.regalo_libre.favorites;

import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.utils.DtoConverter;
import lombok.Builder;

import java.util.List;

@Builder
public record FavoritesDTO(
        String currency_id,
        String id,
        String permalink,
        Long price,
        String status,
        List<String> subStatus,
        String thumbnail,
        String title
) implements DtoConverter<BookmarkedProduct, FavoritesDTO> {
    @Override
    public FavoritesDTO toDto(BookmarkedProduct product) {
        return FavoritesDTO.builder()
                .id(product.getId())
                .currency_id(product.getCurrencyId())
                .permalink(product.getPermalink())
                .price(product.getPrice())
                .status(product.getStatus())
                .subStatus(product.getSubStatus())
                .thumbnail(product.getThumbnail())
                .title(product.getTitle())
                .build();
    }
}
