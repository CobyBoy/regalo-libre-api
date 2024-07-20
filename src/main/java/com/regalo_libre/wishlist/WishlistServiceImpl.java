package com.regalo_libre.wishlist;

import com.regalo_libre.auth.repository.OAuthUserRepository;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.mercadolibre.bookmark.BookmarkRepository;
import com.regalo_libre.mercadolibre.auth.exception.UserNotFoundException;
import com.regalo_libre.wishlist.exception.GiftAlreadyInWishlistException;
import com.regalo_libre.wishlist.exception.PublicWishListNotFoundException;
import com.regalo_libre.wishlist.exception.WishlistNotFoundException;
import com.regalo_libre.wishlist.model.WishList;
import com.regalo_libre.wishlist.dto.EditListDTO;
import com.regalo_libre.wishlist.dto.WishListCreateRequestDto;
import com.regalo_libre.wishlist.dto.WishListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements IWishlistService {
    private final WishlistRepository wishlistRepository;
    private final BookmarkRepository mercadoLibreProductRepo;
    private final OAuthUserRepository oAuthUserRepository;

    public WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId) {
        var oAuthUser = oAuthUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        WishList wishList = wishlistRepository.save(
                WishList.builder()
                        .name(wishListRequest.name())
                        .description(wishListRequest.description())
                        .isPrivate(wishListRequest.isPrivate())
                        .gifts(new ArrayList<>())
                        .user(oAuthUser)
                        .build());

        return WishListDto.builder().build().toDto(wishList);
    }

    public List<WishListDto> getWishListsByUserId(Long userId) {
        var oAuthUser = oAuthUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        List<WishList> wishLists = wishlistRepository.findAllByUser(oAuthUser);
        return wishLists.stream()
                .map(wishList -> WishListDto.builder().build().toDto(wishList))
                .toList();
    }

    public WishListDto findWishlistById(Long id) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        return WishListDto.builder().build().toDto(wishList);
    }

    public void deleteWishlistById(Long id) {
        WishList wishlistToDelete = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        wishlistRepository.deleteById(wishlistToDelete.getId());
    }

    public WishListDto updateWishlistById(Long id, EditListDTO request) {
        WishList wishlistToEdit = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        wishlistToEdit.setName(request.name());
        wishlistToEdit.setDescription(request.description());
        wishlistToEdit.setIsPrivate(request.isPrivate());
        wishlistToEdit.setUpdatedAt(LocalDateTime.now());
        return WishListDto.builder().build().toDto(wishlistRepository.save(wishlistToEdit));
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

    public WishListDto getPublicWishlistByUserId(String id) {
        var publicWishList = wishlistRepository.findByPublicIdAndIsPrivateFalse(id);
        if (publicWishList == null) {
            throw new PublicWishListNotFoundException("La lista no existe o no es pública");
        }
        return WishListDto.builder().build().toDto(publicWishList);
    }

    public List<WishListDto> getAllPublicWishlistsByUserId(Long userId) {
        var oAuthUser = oAuthUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        return wishlistRepository.findAllByUserAndIsPrivateFalse(oAuthUser).stream().map(wishList -> WishListDto.builder().build().toDto(wishList)).toList();
    }

    public List<WishListDto> getAllPublicWishlistsByNickname(String nickname) {
        return wishlistRepository.findAllByUserNicknameAndIsPrivateFalse(nickname).stream().map(wishList -> WishListDto.builder().build().toDto(wishList)).toList();
    }
}
