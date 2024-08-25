package com.regalo_libre.bookmarks;

import com.regalo_libre.bookmarks.dto.BookmarkDTO;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;

import java.util.List;
import java.util.stream.Collectors;

public class BookmarkMapper {
    public static BookmarkDTO toDto(BookmarkedProduct entity) {
        return new BookmarkDTO() {
            @Override
            public String getId() {
                return entity.getId();
            }

            @Override
            public String getCurrencyId() {
                return entity.getCurrencyId();
            }

            @Override
            public String getPermalink() {
                return entity.getPermalink();
            }

            @Override
            public Long getPrice() {
                return entity.getPrice();
            }

            @Override
            public String getStatus() {
                return entity.getStatus();
            }

            @Override
            public List<String> getSubStatus() {
                return entity.getSubStatus();
            }

            @Override
            public String getThumbnail() {
                return entity.getThumbnail();
            }

            @Override
            public String getTitle() {
                return entity.getTitle();
            }
        };
    }

    public static List<BookmarkDTO> toDtoList(List<BookmarkedProduct> entities) {
        return entities.stream()
                .map(BookmarkMapper::toDto)
                .collect(Collectors.toList());
    }
}
