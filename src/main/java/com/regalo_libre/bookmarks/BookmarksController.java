package com.regalo_libre.bookmarks;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/favorites")
@RequiredArgsConstructor
public class BookmarksController {
    private final IBookmarksService favoritesService;

    @GetMapping
    public ResponseEntity<Page<BookmarkDTO>> getAllFavorites(@AuthenticationPrincipal Long userId,
                                                             @RequestParam int page,
                                                             @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(favoritesService.getAllBookmarks(userId, pageable));
    }
}
