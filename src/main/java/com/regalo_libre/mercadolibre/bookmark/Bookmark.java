package com.regalo_libre.mercadolibre.bookmark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bookmark {
    @JsonProperty("item_id")
    private String itemId;
    @JsonProperty("bookmarked_date")
    private String bookmarkedDate;
}
