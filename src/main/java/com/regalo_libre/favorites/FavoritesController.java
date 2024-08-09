package com.regalo_libre.favorites;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/favorites")
@RequiredArgsConstructor
public class FavoritesController {
    private final IFavoritesService favoritesService;

    @GetMapping
    public ResponseEntity<List<FavoritesDTO>> getAllFavorites(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(favoritesService.getAllFavorites(userId));
    }
}
