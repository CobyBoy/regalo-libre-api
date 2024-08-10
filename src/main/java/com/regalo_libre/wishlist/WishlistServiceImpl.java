package com.regalo_libre.wishlist;

import com.regalo_libre.auth.OAuthUserService;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.mercadolibre.bookmark.BookmarkRepository;
import com.regalo_libre.wishlist.dto.*;
import com.regalo_libre.wishlist.exception.GiftAlreadyInWishlistException;
import com.regalo_libre.wishlist.exception.PublicWishListNotFoundException;
import com.regalo_libre.wishlist.exception.WishlistNotFoundException;
import com.regalo_libre.wishlist.model.WishList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final BookmarkRepository mercadoLibreProductRepo;
    private final OAuthUserService oAuthUserService;

    public WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId) {
        long startTime = System.nanoTime();
        var oAuthUser = oAuthUserService.getOAuthUserById(userId);
        long userFetchTime = System.nanoTime();
        log.info("start and user ftech time {}", (userFetchTime - startTime) / 1_000_000.0 + "ms");
        WishList wishList = wishlistRepository.save(
                WishList.builder()
                        .name(wishListRequest.name())
                        .description(wishListRequest.description())
                        .isPrivate(wishListRequest.isPrivate())
                        .gifts(Collections.emptyList())
                        .user(oAuthUser)
                        .build());
        long saveTime = System.nanoTime();
        long endTime = System.nanoTime();
        log.info("save user and user dto time {}", (endTime - saveTime) / 1_000_000.0 + "ms");
        return new WishListDto(wishList);
    }

    public List<WishListDto> getAllWishlists(Long userId) {
        return wishlistRepository.findAllByUserId(userId).stream().map(WishListDto::new).toList();
    }

    public WishListDetailDto findWishlistById(Long id) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        return WishListDetailDto.builder().build().toDto(wishList);
    }

    public void deleteWishlistById(Long id) {
        wishlistRepository.deleteById(id);
    }

    public WishListDto updateWishlistById(Long id, EditListDTO request) {
        WishList wishlistToEdit = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        wishlistToEdit.setName(request.name());
        wishlistToEdit.setDescription(request.description());
        wishlistToEdit.setIsPrivate(request.isPrivate());
        wishlistToEdit.setUpdatedAt(LocalDateTime.now());
        return new WishListDto(wishlistRepository.save(wishlistToEdit));
    }

    public void addProductsToWishlist(Long id, List<String> productsIds) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        List<BookmarkedProduct> newProducts = mercadoLibreProductRepo.findAllByIdIn(productsIds);
        List<BookmarkedProduct> existingProducts = wishList.getGifts();
        Set<String> existingProductIds = existingProducts.stream()
                .map(BookmarkedProduct::getId)
                .collect(Collectors.toSet());

        for (BookmarkedProduct newProduct : newProducts) {
            if (existingProductIds.contains(newProduct.getId())) {
                throw new GiftAlreadyInWishlistException("El producto " + newProduct.getTitle() + " ya está en la lista " + wishList.getName());
            }
        }
        wishList.setUpdatedAt(LocalDateTime.now());

        existingProducts.addAll(newProducts);
        wishlistRepository.save(wishList);
    }

    public void removeProductsFromWishList(Long id, List<String> productsIds) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow();
        List<BookmarkedProduct> productsToDelete = mercadoLibreProductRepo.findAllByIdIn(productsIds);
        var wishListProducts = wishList.getGifts();
        wishListProducts.removeAll(productsToDelete);
        wishList.setUpdatedAt(LocalDateTime.now());
        wishlistRepository.save(wishList);
    }

    public WishListDetailDto findPublicWishlistById(String id) {
        var publicWishList = wishlistRepository.findByPublicIdAndIsPrivateFalse(id);
        if (publicWishList == null) {
            throw new PublicWishListNotFoundException("La lista no existe o no es pública");
        }
        return WishListDetailDto.builder().build().toDto(publicWishList);
    }

    public List<WishListDto> findAllPublicWishlistsByUserId(Long userId) {
        return wishlistRepository.findAllByUserIdAndIsPrivateFalse(userId).stream().map(WishListDto::new).toList();
    }

    public List<WishListDto> findAllPublicWishlistsByUserNickname(String nickname) {
        return wishlistRepository.findPublicWishlistForPublicProfile(nickname).stream().map(WishListDto::new).toList();
    }
}
