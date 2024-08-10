package com.regalo_libre.wishlist;


import com.regalo_libre.wishlist.dto.*;
import lombok.RequiredArgsConstructor;
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

    @GetMapping()
    public ResponseEntity<List<WishListDto>> getAllWishlists(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(wishListService.getAllWishlists(userId));
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
    public ResponseEntity<Void> addProductsToWishList(@PathVariable Long id, @RequestBody List<String> productsIds) {
        wishListService.addProductsToWishlist(id, productsIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/gifts")
    public ResponseEntity<Void> removeProductsFromWishList(@PathVariable Long id, @RequestBody List<String> productsIds) {
        wishListService.removeProductsFromWishList(id, productsIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public")
    public ResponseEntity<List<WishListDto>> findAllPublicWishlistsByUserId(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(wishListService.findAllPublicWishlistsByUserId(userId));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<WishListDetailDto> findPublicWishlist(@PathVariable String id) {
        return ResponseEntity.ok(wishListService.findPublicWishlistById(id));
    }

    @GetMapping("/public/user/{nickname}")
    public ResponseEntity<List<WishListDto>> findAllPublicWishlistsByNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(wishListService.findAllPublicWishlistsByUserNickname(nickname));
    }

}
