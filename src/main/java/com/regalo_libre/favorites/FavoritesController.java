package com.regalo_libre.favorites;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/favorites")
@RequiredArgsConstructor
public class FavoritesController {
    private final IFavoritesService favoritesService;

    @GetMapping
    public ResponseEntity<List<FavoritesDTO>> getFavorites(@RequestParam Long userId) {
        return ResponseEntity.ok(favoritesService.getAllFavorites(userId));
    }
}
