package com.regalo_libre.bookmarks.impl;

import com.regalo_libre.bookmarks.BookmarksService;
import com.regalo_libre.bookmarks.BookmarksSynchronizationService;
import com.regalo_libre.bookmarks.dto.BookmarkDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarksServiceImpl implements BookmarksService {
    private final CacheManager cacheManager;
    private final BookmarksSynchronizationService bookmarksSyncService;

    @Override
    public Page<BookmarkDTO> fetchAllBookmarks(Long userId, Pageable pageable) {
        String cacheKey = generateCacheKey(userId, pageable);
        var cache = cacheManager.getCache("userBookmarksCache");
        Cache.ValueWrapper cachedBookmarks = cache.get(cacheKey);

        if (cachedBookmarks != null) {
            return (Page<BookmarkDTO>) cachedBookmarks.get();
        } else {
            return bookmarksSyncService.saveAllInitialBookmarks(userId, pageable);
        }
    }

    private String generateCacheKey(Long userId, Pageable pageable) {
        return userId + "_" + pageable.getPageNumber() + "_" + pageable.getPageSize();
    }

}
