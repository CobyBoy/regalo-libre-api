package com.regalo_libre.favorites;

import com.regalo_libre.auth.OAuthUserService;
import com.regalo_libre.auth.model.OAuthUser;
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
public class FavoritesServiceImpl implements IFavoritesService {
    private final BookmarkRepository bookmarkRepository;
    private final IMercadoLibreAccessTokenService mercadoLibreAccessTokenService;
    private final OAuthUserService oAuthUserService;

    @Cacheable(value = "userBookmarks", key = "'user_' + #userId", unless = "#result == null")
    public List<FavoritesDTO> getAllFavorites(Long userId) {
        MercadoLibreAccessToken token = mercadoLibreAccessTokenService.getMercadoLibreAccessToken(userId);
        OAuthUser user = oAuthUserService.findUserById(userId);
        // Get bookmarks from meli for this user
        List<BookmarkedProduct> userApiBookmarks = getAllBookmarks(token);
        // Get all from bookmark table, not only from the user
        List<BookmarkedProduct> allExistingBookmarks = bookmarkRepository.findAll();
        return checkForApiBookmarksAndSavedInDbBookmarks(userApiBookmarks, allExistingBookmarks, user);
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

    private List<FavoritesDTO> checkForApiBookmarksAndSavedInDbBookmarks(List<BookmarkedProduct> userApiBookmarks,
                                                                         List<BookmarkedProduct> allExistingBookmarks,
                                                                         OAuthUser user) {
        if (userApiBookmarks.isEmpty() && allExistingBookmarks.isEmpty()) {
            // If no api products and no bookmarks
            return new ArrayList<>();
        }
        return synchronizeBookmarks(user, userApiBookmarks, allExistingBookmarks);
    }

    @Transactional
    private List<FavoritesDTO> synchronizeBookmarks(OAuthUser user,
                                                    List<BookmarkedProduct> userApiBookmarks,
                                                    List<BookmarkedProduct> allExistingBookmarks) {
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
            oAuthUserService.saveOAuthUser(user);
        }
        removeBookmarksThatDontBelongToAnyUser();
        return getFavoritesDto(user.getId());
    }

    private void saveMeLiBookmarksIfDbIsEmpty(OAuthUser user,
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
                                                         OAuthUser user) {
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

    private List<FavoritesDTO> getFavoritesDto(Long userId) {
        return bookmarkRepository.findMercadoLibreProductsByUserId(userId)
                .stream()
                .map(product -> FavoritesDTO.builder().build().toDto(product))
                .toList();
    }

    private void saveNewBookmarksAddedInMeli(OAuthUser user,
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

    private void removeBookmarksThatHaveBeenRemovedFromMeli(OAuthUser user, List<BookmarkedProduct> allExistingBookmarks, Map<String, BookmarkedProduct> existingProductMap, Map<String, BookmarkedProduct> apiProductMap) {
        // Get the current bookmarks that are not in the api and remove them to be in sync
        List<BookmarkedProduct> bookmarksToRemoveForThisUser = getListOfBookmarksToRemoveThatAreNotOnMeli(allExistingBookmarks, existingProductMap, apiProductMap);
        if (!bookmarksToRemoveForThisUser.isEmpty()) {
            user.getBookmarkedProducts().removeAll(bookmarksToRemoveForThisUser);
            oAuthUserService.saveOAuthUser(user);
        }
    }

    private void removeBookmarksThatDontBelongToAnyUser() {
        var bookmarkWithoutUser = bookmarkRepository.findBookmarkWithoutUser();
        if (!bookmarkWithoutUser.isEmpty()) {
            bookmarkRepository.deleteAll(bookmarkWithoutUser);
        }
    }
}
