package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.model.dto.EditListDTO;
import com.regalo_libre.wishlist.model.dto.WishListCreateRequestDto;
import com.regalo_libre.wishlist.model.dto.WishListDto;

import java.util.List;

public interface IWishlistService {
    WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId);

    List<WishListDto> getWishListsByUserId(Long userId);

    WishListDto findWishlistById(Long id);

    void deleteWishlistById(Long id);

    WishListDto updateWishlistById(Long id, EditListDTO request);

    WishListDto getPublicWishlistsByUserId(String id);
}
