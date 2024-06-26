package com.meli.wishlist;


import com.meli.wishlist.model.dto.EditListDTO;
import com.meli.wishlist.model.dto.WishListCreateRequestDto;
import com.meli.wishlist.model.dto.WishListDto;
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

    /*@PostMapping()
    public ResponseEntity<WishListDto> createWishlist(@RequestBody WishListDto wishListRequest, @RequestParam Integer userId) {
        return ResponseEntity.ok(wishListService.createWishlist(wishListRequest, userId));
    }*/
    @PostMapping()
    public ResponseEntity<WishListDto> createWishlist(@RequestBody WishListCreateRequestDto wishListRequest, @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListService.createWishlist(wishListRequest, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateWishlist(@PathVariable Long id, @RequestBody EditListDTO request) {
        return ResponseEntity.ok(wishListService.updateWishlistById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable Long id) {
        wishListService.deleteWishlistById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/gifts")
    public ResponseEntity addProductsToWishList(@PathVariable Long id, @RequestBody List<String> productsIds) {
        wishListService.addProductsToWishlist(id, productsIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/gifts")
    public ResponseEntity<Void> removeProductsFromWishList(@PathVariable Long id, @RequestBody List<String> productsIds) {
        wishListService.removeProductsFromWishList(id, productsIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public/{id}")
    public ResponseEntity getPublicWishlistsByUserId(@PathVariable String id) {

        return ResponseEntity.ok(wishListService.getPublicWishlistsByUserId(id));
    }
}
