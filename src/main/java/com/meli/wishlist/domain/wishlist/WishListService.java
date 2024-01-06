package com.meli.wishlist.domain.wishlist;

import com.meli.wishlist.domain.mercadolibre.MercadoLibreProduct;
import com.meli.wishlist.domain.mercadolibre.MercadoLibreProductRepo;
import com.meli.wishlist.domain.wishlist.model.WishList;
import com.meli.wishlist.domain.wishlist.model.dto.EditListDTO;
import com.meli.wishlist.domain.wishlist.model.dto.WishListDto;
import com.meli.wishlist.domain.wishlist.infrastructure.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishlistRepository wishlistRepository;
    private final MercadoLibreProductRepo mercadoLibreProductRepo;

    public WishListDto createWishlist(WishListDto wishListRequest) {
        WishList wishList = wishlistRepository.save(
                WishList.builder()
                        .name(wishListRequest.name())
                        .description(wishListRequest.description())
                        .visibility(wishListRequest.visibility())
                        .products(new ArrayList<>())
                        .build());

        return WishListDto.toDto(wishList);
    }

    public List<WishListDto> getCreatedLists() {
        List<WishList> wishLists = wishlistRepository.findAll();
        return wishLists.stream()
                .map(WishListDto::toDto)
                .toList();
    }

    public WishListDto getWishlistById(Long id) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow();
        return WishListDto.toDto(wishList);
    }

    public List<WishList> deleteListById(Long id) {
        var wishlistToDelete = wishlistRepository.findById(id).orElseThrow();
        wishlistRepository.deleteById(wishlistToDelete.getId());
        return wishlistRepository.findAll();
    }

    public WishList updateListById(Long id, EditListDTO request) {
        WishList wishlistToEdit = wishlistRepository.findById(id).orElseThrow();
        wishlistToEdit.setName(request.name());
        wishlistToEdit.setDescription(request.description());
        wishlistToEdit.setVisibility(request.visibility());
        return wishlistRepository.save(wishlistToEdit);
    }

    public void addProductsToList(Long id, List<String> productsIds) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow();
        List<MercadoLibreProduct> products = mercadoLibreProductRepo.findAllByIdIn(productsIds);
        wishList.setProducts(products);
        wishlistRepository.save(wishList);
    }
}
