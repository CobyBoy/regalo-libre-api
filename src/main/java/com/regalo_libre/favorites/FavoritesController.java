package com.regalo_libre.favorites;

import com.regalo_libre.auth.IKeycloakService;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/favorites")
@CrossOrigin(origins = "https://localhost:4200")
@RequiredArgsConstructor
public class FavoritesController {
    private final IFavoritesService favoritesService;

    @GetMapping
    public ResponseEntity<List<BookmarkedProduct>> getFavorites(@RequestParam Long userId) {
        return ResponseEntity.ok(favoritesService.getAllFavorites(userId));
    }
}
