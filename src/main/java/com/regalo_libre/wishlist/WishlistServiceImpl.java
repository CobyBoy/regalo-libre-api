package com.regalo_libre.wishlist;

import com.regalo_libre.auth.Auth0UserService;
import com.regalo_libre.auth.model.Auth0User;
import com.regalo_libre.mercadolibre.auth.exception.UserNotFoundException;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.mercadolibre.bookmark.BookmarkRepository;
import com.regalo_libre.wishlist.dto.*;
import com.regalo_libre.wishlist.exception.*;
import com.regalo_libre.wishlist.model.WishList;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {
    public static final String LA_LISTA_NO_EXISTE = "La lista no existe";
    private final WishlistRepository wishlistRepository;
    private final BookmarkRepository mercadoLibreProductRepo;
    private final Auth0UserService auth0UserService;

    @Transactional
    public WishListDto createWishlist(WishListCreateRequestDto wishListRequest, Long userId) {
        long startTime = System.nanoTime();
        Auth0User auth0User = auth0UserService.getAuth0UserById(userId);
        long userFetchTime = System.nanoTime();
        log.info("start and user ftech time {}", (userFetchTime - startTime) / 1_000_000.0 + "ms");
        WishList wishListByName = wishlistRepository.findByNameAndUserId(wishListRequest.name(), userId);
        if (wishListByName != null) {
            throw new WishlistWithSameNameAlreadyExistsException("Ya existe una lista con este nombre");
        }
        WishList wishList = wishlistRepository.save(
                WishList.builder()
                        .name(wishListRequest.name())
                        .description(wishListRequest.description())
                        .isPrivate(wishListRequest.isPrivate())
                        .gifts(Collections.emptyList())
                        .user(auth0User)
                        .build());
        long saveTime = System.nanoTime();
        long endTime = System.nanoTime();
        log.info("save user and user dto time {}", (endTime - saveTime) / 1_000_000.0 + "ms");
        return new WishListDto(wishList);
    }

    public Page<DashboardWishlistDto> getAllWishlistsForDashboard(Long userId, Pageable pageable) {
        boolean auth0User = auth0UserService.existsById(userId);
        if (!auth0User) throw new UserNotFoundException("Usuario no encontrado");
        return wishlistRepository.findAllWishlistForDashboardByUserId(userId, pageable);
    }

    @Override
    public List<WishlistDetailForModalDto> getAllWishlistsForModal(Long userId) {
        return wishlistRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream().map(WishlistDetailForModalDto::new).toList();
    }

    public WishListDetailDto findWishlistById(Long id) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException(LA_LISTA_NO_EXISTE));
        return WishListDetailDto.builder().build().toDto(wishList);
    }

    @Transactional
    public void deleteWishlistById(Long id) {
        try {
            wishlistRepository.deleteById(id);
        } catch (Exception e) {
            throw new UnableToDeleteWishlistException("Ocurrió un error. La lista no se pudo borrar");
        }
    }

    @Transactional
    public WishListDto updateWishlistById(Long id, EditListDTO request) {
        WishList wishlistToEdit = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException(LA_LISTA_NO_EXISTE));
        wishlistToEdit.setName(request.name());
        wishlistToEdit.setDescription(request.description());
        wishlistToEdit.setIsPrivate(request.isPrivate());
        wishlistToEdit.setUpdatedAt(LocalDateTime.now());
        return new WishListDto(wishlistRepository.save(wishlistToEdit));
    }

    public AddProductsResponse addProductsToWishlist(Long id, List<String> productsIds) {
        WishList wishList = wishlistRepository.findById(id).orElseThrow(() -> new WishlistNotFoundException(LA_LISTA_NO_EXISTE));
        List<BookmarkedProduct> newProducts = mercadoLibreProductRepo.findAllByIdIn(productsIds);
        List<BookmarkedProduct> existingProducts = wishList.getGifts();
        Set<String> existingProductIds = existingProducts.stream()
                .map(BookmarkedProduct::getId)
                .collect(Collectors.toSet());
        List<String> errors = new ArrayList<>();
        List<String> productsToAdd = new ArrayList<>();

        for (BookmarkedProduct newProduct : newProducts) {
            if (existingProductIds.contains(newProduct.getId())) {
                errors.add(newProduct.getTitle() + " ya está en la lista " + wishList.getName());
            } else {
                existingProducts.add(newProduct);
                productsToAdd.add(newProduct.getTitle() + "agregado a lista " + wishList.getName());
            }
        }

        wishList.setUpdatedAt(LocalDateTime.now());
        wishlistRepository.save(wishList);

        AddProductsResponse addProductsResponse = new AddProductsResponse();
        addProductsResponse.setProductsAdded(productsToAdd);
        addProductsResponse.setErrors(errors);
        return addProductsResponse;
    }

    @Transactional
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
        return wishlistRepository.findAllByUserIdAndIsPrivateFalseOrderByCreatedAtDesc(userId).stream().map(WishListDto::new).toList();
    }

    public List<WishListDto> findAllPublicWishlistsByUserNickname(String nickname) {
        return wishlistRepository.findPublicWishlistForPublicProfile(nickname).stream().map(WishListDto::new).toList();
    }

    @Getter
    @Setter
    public static class AddProductsResponse {
        private List<String> productsAdded;
        private List<String> errors;
    }
}
