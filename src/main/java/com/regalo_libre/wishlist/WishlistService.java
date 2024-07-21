package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.dto.EditListDTO;
import com.regalo_libre.wishlist.dto.WishListCreateRequestDto;
import com.regalo_libre.wishlist.dto.WishListDto;

import java.util.List;

public interface WishlistService {
    WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId);

    List<WishListDto> getAllWishListsByUserId(Long userId);

    WishListDto getWishlistById(Long id);

    WishListDto updateWishlistById(Long id, EditListDTO request);

    void deleteWishlistById(Long id);

    void addProductsToWishlist(Long id, List<String> productsIds);

    void removeProductsFromWishList(Long id, List<String> productsIds);

    WishListDto getPublicWishlistByUserId(String id);

    List<WishListDto> getAllPublicWishlistsByUserId(Long userId);

    List<WishListDto> getAllPublicWishlistsByUserNickname(String nickname);
}
