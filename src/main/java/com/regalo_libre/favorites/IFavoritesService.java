package com.regalo_libre.favorites;

import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;

import java.util.List;

public interface IFavoritesService {
    List<BookmarkedProduct> getAllFavorites(Long userId);
}