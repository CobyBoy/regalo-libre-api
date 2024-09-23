package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishlistService {
    WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId);

    Page<DashboardWishlistDto> getAllWishlistsForDashboard(Long userId, Pageable pageable);

    List<WishlistDetailForModalDto> getAllWishlistsForModal(Long userId);

    WishListDetailDto findWishlistById(Long id, Pageable pageable);

    WishListDto updateWishlistById(Long id, EditListDTO request);

    void deleteWishlistById(Long id);

    WishlistServiceImpl.AddProductsResponse addProductsToWishlist(Long id, List<String> productsIds);

    void removeProductsFromWishList(Long id, List<String> productsIds);

    WishListDetailDto findPublicWishlistById(Long id);

    Page<PublicWishlistDto> findAllPublicWishlistsByUserId(Long userId, Pageable pageable);

    Page<PublicProfileWishlistDto> findAllPublicWishlistsByUserNickname(String nickname, Pageable pageable);
}
