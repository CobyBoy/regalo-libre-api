package com.regalo_libre.bookmarks.dto;

public record BookmarkDetailsDto(
        String currencyId,
        String id,
        String permalink,
        Long price,
        String status,
        String thumbnail,
        String title
) {
}
