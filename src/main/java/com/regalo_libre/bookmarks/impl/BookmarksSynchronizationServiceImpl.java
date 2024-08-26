package com.regalo_libre.bookmarks.impl;

import com.regalo_libre.auth.Auth0UserService;
import com.regalo_libre.auth.model.Auth0User;
import com.regalo_libre.bookmarks.BookmarkApiService;
import com.regalo_libre.bookmarks.BookmarksSynchronizationService;
import com.regalo_libre.bookmarks.dto.BookmarkDTO;
import com.regalo_libre.mercadolibre.auth.MercadoLibreAccessTokenService;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.bookmark.BookmarkRepository;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarksSynchronizationServiceImpl implements BookmarksSynchronizationService {
    private final Auth0UserService auth0UserService;
    private final MercadoLibreAccessTokenService mercadoLibreAccessTokenService;
    private final BookmarkApiService bookmarkApiService;
    private final BookmarkRepository bookmarkRepository;
    private final CacheManager cacheManager;

    @Transactional
    public Page<BookmarkDTO> saveAllInitialBookmarks(Long userId, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 15); // Default to page 0 with 15 items per page
        }
        String cacheKey = generateCacheKey(userId, pageable);
        var cache = cacheManager.getCache("userBookmarksCache");
        MercadoLibreAccessToken token = mercadoLibreAccessTokenService.getMercadoLibreAccessToken(userId);
        Auth0User user = auth0UserService.findAuth0UserById(userId);
        checkForApiBookmarksAndSavedInDbBookmarks(user, token);
        var newBookmarks = bookmarkRepository.findMercadoLibreProductsByUserId(userId, pageable);
        if (cache != null) {
            cache.put(cacheKey, newBookmarks);
            return newBookmarks;
        }

        return newBookmarks;
    }

    private String generateCacheKey(Long userId, Pageable pageable) {
        return userId + "_" + pageable.getPageNumber() + "_" + pageable.getPageSize();
    }

    @Transactional
    private List<BookmarkedProduct> checkForApiBookmarksAndSavedInDbBookmarks(Auth0User user,
                                                                              MercadoLibreAccessToken token) {
        List<BookmarkedProduct> userApiBookmarks = bookmarkApiService.getAllBookmarks(token);
        // Get all from bookmark table, not only from the user
        List<BookmarkedProduct> allExistingBookmarks = bookmarkRepository.findAll();
        if (userApiBookmarks.isEmpty() && allExistingBookmarks.isEmpty()) {
            // If no api products and no bookmarks
            return new ArrayList<>();
        }
        synchronizeBookmarks(user, userApiBookmarks, allExistingBookmarks);
        return bookmarkRepository.findAll();
    }

    @Transactional
    private void synchronizeBookmarks(Auth0User user,
                                      List<BookmarkedProduct> userApiBookmarks,
                                      List<BookmarkedProduct> allExistingBookmarks
    ) {
        saveMeLiBookmarksIfDbIsEmpty(user, userApiBookmarks, allExistingBookmarks);

        //list of current bookmarks ids MLA
        Map<String, BookmarkedProduct> existingProductMap = allExistingBookmarks.stream()
                .collect(Collectors.toMap(BookmarkedProduct::getId, product -> product));
        //list of api bookmarks ids MLA
        Map<String, BookmarkedProduct> apiProductMap = userApiBookmarks.stream()
                .collect(Collectors.toMap(BookmarkedProduct::getId, product -> product));

        // Get the bookmarks in common and check if this user does not have them to add to it later
        List<BookmarkedProduct> sharedBookmarks = getSharedBookmarks(allExistingBookmarks, apiProductMap, user);

        saveNewBookmarksAddedInMeli(user, allExistingBookmarks, userApiBookmarks, existingProductMap);
        removeBookmarksThatHaveBeenRemovedFromMeli(user, allExistingBookmarks, existingProductMap, apiProductMap);

        if (!sharedBookmarks.isEmpty()) {
            for (BookmarkedProduct sharedBookmark : sharedBookmarks) {
                user.getBookmarkedProducts().add(sharedBookmark);
            }
            auth0UserService.saveAuth0User(user);
        }
        removeBookmarksThatDontBelongToAnyUser();
    }

    @Transactional
    private void saveMeLiBookmarksIfDbIsEmpty(Auth0User user,
                                              List<BookmarkedProduct> userApiBookmarks,
                                              List<BookmarkedProduct> allExistingBookmarks) {
        // if no bookmarks in db, save the ones from the api and also set it to the current user
        if (allExistingBookmarks.isEmpty()) {
            userApiBookmarks.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            bookmarkRepository.saveAll(userApiBookmarks);
        }
    }

    private List<BookmarkedProduct> getSharedBookmarks(List<BookmarkedProduct> allExistingBookmarks,
                                                       Map<String, BookmarkedProduct> apiProductMap,
                                                       Auth0User user) {
        List<BookmarkedProduct> sharedBookmarks = new ArrayList<>();
        for (BookmarkedProduct existingBookmark : allExistingBookmarks) {
            BookmarkedProduct inCommonBookmark = apiProductMap.get(existingBookmark.getId());

            if (inCommonBookmark != null) {
                var matchingUsers = existingBookmark.getUsers().stream().filter(userWithBookmark -> userWithBookmark.getId().equals(user.getId())).toList();
                if (matchingUsers.isEmpty()) {
                    sharedBookmarks.add(existingBookmark);
                }
            }
        }
        return sharedBookmarks;
    }

    @Transactional
    private void saveNewBookmarksAddedInMeli(Auth0User user,
                                             List<BookmarkedProduct> allExistingBookmarks,
                                             List<BookmarkedProduct> userApiBookmarks, Map<String,
            BookmarkedProduct> existingProductMap) {
        // From the api bookmark, get the bookmarks that are not in the db to add them because they have been added in MeLi
        List<BookmarkedProduct> productsToSaveOrUpdate = getListOfNewBookmarksThatHaveBeenAddedToMeli(userApiBookmarks, existingProductMap);
        if (!productsToSaveOrUpdate.isEmpty() && !allExistingBookmarks.isEmpty()) {
            productsToSaveOrUpdate.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            bookmarkRepository.saveAll(productsToSaveOrUpdate);
        }
    }

    private List<BookmarkedProduct> getListOfNewBookmarksThatHaveBeenAddedToMeli(List<BookmarkedProduct> userApiBookmarks,
                                                                                 Map<String, BookmarkedProduct> existingProductMap) {
        // From the api bookmark, get the bookmarks that are not in the db to add them because they have been added in MeLi
        List<BookmarkedProduct> productsToSaveOrUpdate = new ArrayList<>();
        for (BookmarkedProduct apiProduct : userApiBookmarks) {
            BookmarkedProduct existingProduct = existingProductMap.get(apiProduct.getId());
            if (existingProduct == null) {
                // New product.
                productsToSaveOrUpdate.add(apiProduct);
            }
        }
        return productsToSaveOrUpdate;
    }

    @Transactional
    private void removeBookmarksThatHaveBeenRemovedFromMeli(Auth0User user, List<BookmarkedProduct> allExistingBookmarks,
                                                            Map<String, BookmarkedProduct> existingProductMap,
                                                            Map<String, BookmarkedProduct> apiProductMap) {
        // Get the current bookmarks that are not in the api and remove them to be in sync
        List<BookmarkedProduct> bookmarksToRemoveForThisUser = getListOfBookmarksToRemoveThatAreNotOnMeli(allExistingBookmarks, existingProductMap, apiProductMap);
        if (!bookmarksToRemoveForThisUser.isEmpty()) {
            user.getBookmarkedProducts().removeAll(bookmarksToRemoveForThisUser);
            auth0UserService.saveAuth0User(user);
        }
    }

    private List<BookmarkedProduct> getListOfBookmarksToRemoveThatAreNotOnMeli(List<BookmarkedProduct> allExistingBookmarks,
                                                                               Map<String, BookmarkedProduct> existingProductMap,
                                                                               Map<String, BookmarkedProduct> apiProductMap) {
        // Get the current bookmarks that are not in the api and remove them to be in sync
        List<BookmarkedProduct> bookmarksToRemoveForThisUser = new ArrayList<>();
        for (BookmarkedProduct bookmarkedProduct : allExistingBookmarks) {
            BookmarkedProduct existingProduct = apiProductMap.get(bookmarkedProduct.getId());
            if (existingProduct == null) {
                // Product to remove
                bookmarksToRemoveForThisUser.add(existingProductMap.get(bookmarkedProduct.getId()));
            }
        }
        return bookmarksToRemoveForThisUser;
    }

    @Transactional
    private void removeBookmarksThatDontBelongToAnyUser() {
        var bookmarkWithoutUser = bookmarkRepository.findBookmarkWithoutUser();
        if (!bookmarkWithoutUser.isEmpty()) {
            bookmarkRepository.deleteAll(bookmarkWithoutUser);
        }
    }
}
