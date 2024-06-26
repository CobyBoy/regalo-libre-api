package com.meli.wishlist.wishlist;

import com.meli.wishlist.wishlist.model.dto.EditListDTO;
import com.meli.wishlist.wishlist.model.dto.WishListCreateRequestDto;
import com.meli.wishlist.wishlist.model.dto.WishListDto;

import java.util.List;

public interface IWishlistService {
    WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId);

    List<WishListDto> getWishListsByUserId(Long userId);

    WishListDto findWishlistById(Long id);

    void deleteWishlistById(Long id);

    WishListDto updateWishlistById(Long id, EditListDTO request);

    WishListDto getPublicWishlistsByUserId(String id);
}
