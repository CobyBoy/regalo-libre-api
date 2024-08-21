package com.regalo_libre.bookmarks;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;

import java.util.List;

public interface BookmarkApiService {
    List<BookmarkedProduct> getAllBookmarks(MercadoLibreAccessToken token);
}
