package com.regalo_libre.bookmarks.dto;

import java.util.List;

public interface BookmarkDTO {
    String getId();

    String getCurrencyId();

    String getPermalink();

    Long getPrice();

    String getStatus();

    List<String> getSubStatus();

    String getThumbnail();

    String getTitle();
};
