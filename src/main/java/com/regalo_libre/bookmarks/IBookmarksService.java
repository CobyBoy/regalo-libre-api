package com.regalo_libre.bookmarks;

import com.regalo_libre.bookmarks.dto.BookmarkDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookmarksService {
    Page<BookmarkDTO> getAllBookmarks(Long userId, Pageable pageable);
}
