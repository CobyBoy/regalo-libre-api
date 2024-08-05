package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.dto.*;

import java.util.List;

public interface WishlistService {
    WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId);

    List<WishListDto> getAllWishListsByUserId(Long userId);

    WishListDetailDto getWishlistById(Long id);

    WishListDto updateWishlistById(Long id, EditListDTO request);

    void deleteWishlistById(Long id);

    void addProductsToWishlist(Long id, List<String> productsIds);

    void removeProductsFromWishList(Long id, List<String> productsIds);

    WishListDetailDto getPublicWishlistByUserId(String id);

    List<WishListDto> getAllPublicWishlistsByUserId(Long userId);

    List<WishListDto> getAllPublicWishlistsByUserNickname(String nickname);
}
