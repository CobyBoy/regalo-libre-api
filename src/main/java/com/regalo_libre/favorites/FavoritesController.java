package com.regalo_libre.favorites;

import com.regalo_libre.auth.IKeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/favorites")
@CrossOrigin(origins = "https://localhost:4200")
@RequiredArgsConstructor
public class FavoritesController {
    private final FavoritesService favoritesService;
    private final IKeycloakService keycloakService;

    @GetMapping
    public ResponseEntity getFavorites(@RequestParam Long userId) {
        return ResponseEntity.ok(favoritesService.getAllFavorites(userId));
    }
}
