package com.meli.wishlist.domain.mercadolibre;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/favorites")
@CrossOrigin(origins = "https://localhost:4200")
@RequiredArgsConstructor
public class FavoritesController {
    private final FavoritesService favoritesService;

    @GetMapping
    public ResponseEntity getFavorites() {
        return  ResponseEntity.ok(favoritesService.getAllFavorites());
    }
}
