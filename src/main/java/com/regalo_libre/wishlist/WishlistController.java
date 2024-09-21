package com.regalo_libre.wishlist;


import com.regalo_libre.wishlist.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lists")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishListService;

    @GetMapping("/dashboard")
    public ResponseEntity<Page<DashboardWishlistDto>> getAllWishlistsForDashboard(@AuthenticationPrincipal Long userId,
                                                                                  @RequestParam int page,
                                                                                  @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(wishListService.getAllWishlistsForDashboard(userId, pageable));
    }

    @GetMapping("/modal")
    public ResponseEntity getAllWishlistsForAddGiftsModal(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(wishListService.getAllWishlistsForModal(userId));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<WishListDetailDto> findWishlistById(@PathVariable Long id) {
        return ResponseEntity.ok(wishListService.findWishlistById(id));
    }

    @PostMapping()
    public ResponseEntity<WishListDto> createWishlist(@RequestBody WishListCreateRequestDto wishListRequest, @AuthenticationPrincipal Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListService.createWishlist(wishListRequest, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishListDto> updateWishlist(@PathVariable Long id, @RequestBody EditListDTO request) {
        return ResponseEntity.ok(wishListService.updateWishlistById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable Long id) {
        wishListService.deleteWishlistById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/gifts")
    public ResponseEntity<WishlistServiceImpl.AddProductsResponse> addProductsToWishList(@PathVariable Long id, @RequestBody List<String> productsIds) {
        return ResponseEntity.ok(wishListService.addProductsToWishlist(id, productsIds));
    }

    @DeleteMapping("/{id}/gifts")
    public ResponseEntity<Void> removeProductsFromWishList(@PathVariable Long id, @RequestBody List<String> productsIds) {
        wishListService.removeProductsFromWishList(id, productsIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public")
    public ResponseEntity<Page<PublicWishlistDto>> findAllPublicWishlistsByUserId(@AuthenticationPrincipal Long userId,
                                                                                  @RequestParam int page,
                                                                                  @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(wishListService.findAllPublicWishlistsByUserId(userId, pageable));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<WishListDetailDto> findPublicWishlist(@PathVariable String id) {
        return ResponseEntity.ok(wishListService.findPublicWishlistById(id));
    }

    @GetMapping("/public/user/{nickname}")
    public ResponseEntity<Page<PublicProfileWishlistDto>> findAllPublicWishlistsByNickname(@PathVariable String nickname,
                                                                                           @RequestParam int page,
                                                                                           @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(wishListService.findAllPublicWishlistsByUserNickname(nickname, pageable));
    }

}
