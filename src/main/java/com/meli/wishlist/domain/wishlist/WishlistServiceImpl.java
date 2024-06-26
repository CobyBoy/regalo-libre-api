package com.meli.wishlist.domain.wishlist;

import com.meli.wishlist.domain.mercadolibre.model.BookmarkedProduct;
import com.meli.wishlist.domain.mercadolibre.MercadoLibreProductRepository;
import com.meli.wishlist.domain.mercadolibre.auth.meli.MercadoLibreUserRepo;
import com.meli.wishlist.domain.wishlist.exception.ProductAlreadyInWishlistException;
import com.meli.wishlist.domain.wishlist.exception.PublicWishListNotFoundException;
import com.meli.wishlist.domain.wishlist.exception.UserNotFoundException;
import com.meli.wishlist.domain.wishlist.exception.WishlistNotFoundException;
import com.meli.wishlist.domain.wishlist.model.WishList;
import com.meli.wishlist.domain.wishlist.model.dto.EditListDTO;
import com.meli.wishlist.domain.wishlist.model.dto.WishListCreateRequestDto;
import com.meli.wishlist.domain.wishlist.model.dto.WishListDto;
import com.meli.wishlist.domain.wishlist.infrastructure.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements IWishlistService {
    private final WishlistRepository wishlistRepository;
    private final MercadoLibreProductRepository mercadoLibreProductRepo;
    private final MercadoLibreUserRepo mercadoLibreUserRepo;

    public WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId) {
        WishList wishList = wishlistRepository.save(
                WishList.builder()
                        .name(wishListRequest.name())
                        .description(wishListRequest.description())
                        //.visibility(wishListRequest.visibility())
                        .isPrivate(wishListRequest.isPrivate())
                        .gifts(new ArrayList<>())
                        .user(mercadoLibreUserRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado")))
                        .build());

        return WishListDto.toDto(wishList);
    }

    public List<WishListDto> getWishListsByUserId(Long userId) {
        List<WishList> wishLists = wishlistRepository.findAllByUserId(userId);
        return wishLists.stream()
                .map(WishListDto::toDto)
                .toList();
    }

    public WishListDto findWishlistById(Long id) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        return WishListDto.toDto(wishList);
    }

    public void deleteWishlistById(Long id) {
        WishList wishlistToDelete = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        wishlistRepository.deleteById(wishlistToDelete.getId());
    }

    public WishListDto updateWishlistById(Long id, EditListDTO request) {
        WishList wishlistToEdit = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException("La lista no existe"));
        wishlistToEdit.setName(request.name());
        wishlistToEdit.setDescription(request.description());
        //wishlistToEdit.setVisibility(request.visibility());
        wishlistToEdit.setIsPrivate(request.isPrivate());
        return WishListDto.toDto(wishlistRepository.save(wishlistToEdit));
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
                throw new ProductAlreadyInWishlistException("El producto " + newProduct.getTitle() + " ya está en la lista " + wishList.getName());
            }
        }

        existingProducts.addAll(newProducts);
        wishlistRepository.save(wishList);
    }

    public void removeProductsFromWishList(Long id, List<String> productsIds) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow();
        List<BookmarkedProduct> productsToDelete = mercadoLibreProductRepo.findAllByIdIn(productsIds);
        var wishListProducts = wishList.getGifts();
        wishListProducts.removeAll(productsToDelete);
        wishlistRepository.save(wishList);
    }

    public WishListDto getPublicWishlistsByUserId(String id) {
        var publicWishList = wishlistRepository.findByPublicIdAndIsPrivateFalse(id);
        if (publicWishList == null) {
            throw new PublicWishListNotFoundException("La lista no existe o no es pública");
        }
        return WishListDto.toDto(publicWishList);
    }
}
