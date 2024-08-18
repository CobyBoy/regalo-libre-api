package com.regalo_libre.bookmarks;

import com.regalo_libre.auth.Auth0UserService;
import com.regalo_libre.auth.model.Auth0User;
import com.regalo_libre.mercadolibre.auth.*;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.bookmark.Bookmark;
import com.regalo_libre.mercadolibre.bookmark.BookmarkItem;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.mercadolibre.bookmark.BookmarkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarksServiceImpl implements IBookmarksService {
    private final BookmarkRepository bookmarkRepository;
    private final IMercadoLibreAccessTokenService mercadoLibreAccessTokenService;
    private final Auth0UserService auth0UserService;

    @Cacheable(value = "userBookmarks", unless = "#result == null")
    public Page<BookmarkDTO> getAllBookmarks(Long userId, Pageable pageable) {
        MercadoLibreAccessToken token = mercadoLibreAccessTokenService.getMercadoLibreAccessToken(userId);
        Auth0User user = auth0UserService.findAuth0UserById(userId);
        return checkForApiBookmarksAndSavedInDbBookmarks(user, token, pageable);
    }

    private List<BookmarkedProduct> getAllBookmarks(MercadoLibreAccessToken token) {
        WebClient webClient = mercadoLibreAccessTokenService.getWebClientWithABearerToken(token.getAccessToken());
        Flux<Bookmark> bookmarksFlux = fetchBookmarksFromApi(webClient);
        return fetchBookmarksByItemId(bookmarksFlux, webClient);
    }

    private Flux<Bookmark> fetchBookmarksFromApi(WebClient webClient) {
        String bookmarksUrl = "/users/me/bookmarks";
        return webClient.get()
                .uri(bookmarksUrl)
                .retrieve()
                .bodyToFlux(Bookmark.class);
    }

    private List<BookmarkedProduct> fetchBookmarksByItemId(Flux<Bookmark> bookmarksFlux, WebClient webClient) {
        return bookmarksFlux.flatMapSequential(bookmark ->
                        webClient.get()
                                .uri("/items?ids={itemId}", bookmark.getItemId())
                                .retrieve()
                                .bodyToFlux(BookmarkItem.class)
                                .map(bookmarkItem -> {
                                    BookmarkedProduct product = bookmarkItem.body();
                                    product.setBookmarkedDate(bookmark.getBookmarkedDate());
                                    return product;
                                })
                )
                .collectList()
                .block();
    }

    private Page<BookmarkDTO> checkForApiBookmarksAndSavedInDbBookmarks(Auth0User user,
                                                                        MercadoLibreAccessToken token,
                                                                        Pageable pageable) {
        List<BookmarkedProduct> userApiBookmarks = getAllBookmarks(token);
        // Get all from bookmark table, not only from the user
        List<BookmarkedProduct> allExistingBookmarks = bookmarkRepository.findAll();
        if (userApiBookmarks.isEmpty() && allExistingBookmarks.isEmpty()) {
            // If no api products and no bookmarks
            return Page.empty();
        }
        return synchronizeBookmarks(user, userApiBookmarks, allExistingBookmarks, pageable);
    }

    @Transactional
    private Page<BookmarkDTO> synchronizeBookmarks(Auth0User user,
                                                   List<BookmarkedProduct> userApiBookmarks,
                                                   List<BookmarkedProduct> allExistingBookmarks,
                                                   Pageable pageable
    ) {
        saveMeLiBookmarksIfDbIsEmpty(user, userApiBookmarks, allExistingBookmarks);

        //list of current bookmarks ids MLA
        Map<String, BookmarkedProduct> existingProductMap = allExistingBookmarks.stream()
                .collect(Collectors.toMap(BookmarkedProduct::getId, product -> product));
        //list of api bookmarks ids MLA
        Map<String, BookmarkedProduct> apiProductMap = userApiBookmarks.stream()
                .collect(Collectors.toMap(BookmarkedProduct::getId, product -> product));

        // Get the bookmarks in common and check if this user does not have them to add to it later
        List<BookmarkedProduct> productsInCommon = getBookmarksInCommon(allExistingBookmarks, apiProductMap, user);

        saveNewBookmarksAddedInMeli(user, allExistingBookmarks, userApiBookmarks, existingProductMap);
        removeBookmarksThatHaveBeenRemovedFromMeli(user, allExistingBookmarks, existingProductMap, apiProductMap);

        if (!productsInCommon.isEmpty()) {
            for (BookmarkedProduct bookmarkedProduct : productsInCommon) {
                user.getBookmarkedProducts().add(bookmarkedProduct);
            }
            auth0UserService.saveAuth0User(user);
        }
        removeBookmarksThatDontBelongToAnyUser();
        return getFavoritesDto(user.getId(), pageable);
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

    private List<BookmarkedProduct> getBookmarksInCommon(List<BookmarkedProduct> allExistingBookmarks,
                                                         Map<String, BookmarkedProduct> apiProductMap,
                                                         Auth0User user) {
        List<BookmarkedProduct> productsInCommon = new ArrayList<>();
        for (BookmarkedProduct bookmarkedProduct : allExistingBookmarks) {
            BookmarkedProduct inCommonBookmark = apiProductMap.get(bookmarkedProduct.getId());

            if (inCommonBookmark != null) {
                var a = bookmarkedProduct.getUsers().stream().filter(buser -> buser.getId().equals(user.getId())).toList();
                if (a.isEmpty()) {
                    productsInCommon.add(bookmarkedProduct);
                }
            }
        }
        return productsInCommon;
    }

    private Page<BookmarkDTO> getFavoritesDto(Long userId, Pageable pageable) {
        return bookmarkRepository.findMercadoLibreProductsByUserId(userId, pageable);
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

    @Transactional
    private void removeBookmarksThatHaveBeenRemovedFromMeli(Auth0User user, List<BookmarkedProduct> allExistingBookmarks, Map<String, BookmarkedProduct> existingProductMap, Map<String, BookmarkedProduct> apiProductMap) {
        // Get the current bookmarks that are not in the api and remove them to be in sync
        List<BookmarkedProduct> bookmarksToRemoveForThisUser = getListOfBookmarksToRemoveThatAreNotOnMeli(allExistingBookmarks, existingProductMap, apiProductMap);
        if (!bookmarksToRemoveForThisUser.isEmpty()) {
            user.getBookmarkedProducts().removeAll(bookmarksToRemoveForThisUser);
            auth0UserService.saveAuth0User(user);
        }
    }

    @Transactional
    private void removeBookmarksThatDontBelongToAnyUser() {
        var bookmarkWithoutUser = bookmarkRepository.findBookmarkWithoutUser();
        if (!bookmarkWithoutUser.isEmpty()) {
            bookmarkRepository.deleteAll(bookmarkWithoutUser);
        }
    }
}
