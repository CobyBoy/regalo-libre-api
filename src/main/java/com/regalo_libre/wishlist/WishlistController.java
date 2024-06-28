package com.regalo_libre.wishlist;


import com.regalo_libre.wishlist.dto.EditListDTO;
import com.regalo_libre.wishlist.dto.WishListCreateRequestDto;
import com.regalo_libre.wishlist.dto.WishListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@CrossOrigin(origins = "https://localhost:4200")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistServiceImpl wishListService;

    @GetMapping()
    public ResponseEntity<List<WishListDto>> getWishLists(@RequestParam Long userId) {
        return ResponseEntity.ok(wishListService.getWishListsByUserId(userId));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<WishListDto> getWishlistById(@PathVariable Long id) {
        return ResponseEntity.ok(wishListService.findWishlistById(id));
    }

    @PostMapping()
    public ResponseEntity<WishListDto> createWishlist(@RequestBody WishListCreateRequestDto wishListRequest, @RequestParam Long userId) {
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

    @GetMapping("/public/{id}")
    public ResponseEntity<WishListDto> getPublicWishlistsByUserId(@PathVariable String id) {

        return ResponseEntity.ok(wishListService.getPublicWishlistsByUserId(id));
    }
}
