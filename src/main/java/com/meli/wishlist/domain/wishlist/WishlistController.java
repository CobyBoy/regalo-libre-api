package com.meli.wishlist.domain.wishlist;


import com.meli.wishlist.domain.wishlist.model.dto.EditListDTO;
import com.meli.wishlist.domain.wishlist.model.dto.WishListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/list")
@CrossOrigin(origins = "https://localhost:4200")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishListService;

    @GetMapping()
    public ResponseEntity<List<WishListDto>> getLists() {
        return ResponseEntity.ok(wishListService.getCreatedLists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishListDto> getListById(@PathVariable Long id) {
        return ResponseEntity.ok(wishListService.getWishlistById(id));
    }

    @PostMapping()
    public ResponseEntity<WishListDto> createList(@RequestBody WishListDto wishListRequest) {
        return ResponseEntity.ok(wishListService.createWishlist(wishListRequest));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity updateList(@PathVariable Long id, @RequestBody EditListDTO request) {
        wishListService.updateListById(id, request);
        return ResponseEntity.ok(wishListService.updateListById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteList(@PathVariable Long id) {
        return ResponseEntity.ok(wishListService.deleteListById(id));
    }

    @PutMapping("/{id}/product")
    public ResponseEntity addProductsToList(@PathVariable Long id, @RequestBody List<String> productsIds) {
        wishListService.addProductsToList(id, productsIds);
        return ResponseEntity.ok("");
    }
}
