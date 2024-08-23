package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishlistService {
    WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId);

    Page<WishListDto> getAllWishlists(Long userId, Pageable pageable);

    List<WishlistDetailForModalDto> getAllWishlistsForModal(Long userId);

    WishListDetailDto findWishlistById(Long id);

    WishListDto updateWishlistById(Long id, EditListDTO request);

    void deleteWishlistById(Long id);

    WishlistServiceImpl.AddProductsResponse addProductsToWishlist(Long id, List<String> productsIds);

    void removeProductsFromWishList(Long id, List<String> productsIds);

    WishListDetailDto findPublicWishlistById(String id);

    List<WishListDto> findAllPublicWishlistsByUserId(Long userId);

    List<WishListDto> findAllPublicWishlistsByUserNickname(String nickname);
}
