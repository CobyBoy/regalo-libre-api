package com.regalo_libre.favorites;

import com.regalo_libre.mercadolibre.auth.*;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreAccessTokenRepository;
import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreUserRepository;
import com.regalo_libre.mercadolibre.bookmark.Bookmark;
import com.regalo_libre.mercadolibre.bookmark.BookmarkItem;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.mercadolibre.bookmark.BookmarkRepository;
import com.regalo_libre.mercadolibre.auth.exception.TokenNotFoundException;
import com.regalo_libre.mercadolibre.auth.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritesServiceImpl implements IFavoritesService {
    private final BookmarkRepository bookmarkRepository;
    private final MercadoLibreAccessTokenRepository accessTokenRepository;
    private final IMercadoLibreAuthClientService authClientService;
    private final MercadoLibreUserRepository userRepository;

    public List<BookmarkedProduct> getAllFavorites(Long userId) {
        MercadoLibreAccessToken token = accessTokenRepository.findById(userId).orElseThrow(() -> new TokenNotFoundException("Sesion no encontrada"));
        MercadoLibreUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        // Get bookmarks from meli for this user
        List<BookmarkedProduct> userApiBookmarks = getAllBookmarkedProducts(token);
        // Get all from bookmark table, not only from the user
        List<BookmarkedProduct> allExistingBookmarks = bookmarkRepository.findAll();
        if (userApiBookmarks.isEmpty() && allExistingBookmarks.isEmpty()) {
            // If no api products and no bookmarks
            return new ArrayList<>();
        }

        // if no bookmarks, save the ones from the api and also set it to the current user
        if (allExistingBookmarks.isEmpty()) {
            userApiBookmarks.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            bookmarkRepository.saveAll(userApiBookmarks);
        }

        //list of current bookmarks ids MLA
        Map<String, BookmarkedProduct> existingProductMap = allExistingBookmarks.stream()
                .collect(Collectors.toMap(BookmarkedProduct::getId, product -> product));
        //list of api bookmarks ids MLA
        Map<String, BookmarkedProduct> apiProductMap = userApiBookmarks.stream()
                .collect(Collectors.toMap(BookmarkedProduct::getId, product -> product));

        // From the api bookmark, get the bookmarks that are not in the db to add them because they have been added in MeLi
        List<BookmarkedProduct> productsToSaveOrUpdate = new ArrayList<>();
        for (BookmarkedProduct apiProduct : userApiBookmarks) {
            BookmarkedProduct existingProduct = existingProductMap.get(apiProduct.getId());
            if (existingProduct == null) {
                // New product.
                productsToSaveOrUpdate.add(apiProduct);
            }
        }
        // Get the current bookmarks that are not in the api and remove them to be in sync
        List<BookmarkedProduct> bookmarksToRemoveForThisUser = new ArrayList<>();
        for (BookmarkedProduct bookmarkedProduct : allExistingBookmarks) {
            BookmarkedProduct existingProduct = apiProductMap.get(bookmarkedProduct.getId());
            if (existingProduct == null) {
                // Product to remove
                bookmarksToRemoveForThisUser.add(existingProductMap.get(bookmarkedProduct.getId()));
            }
        }

        // Get the bookmarks in common and check if this user does not have them to add to it later
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

        if (!productsToSaveOrUpdate.isEmpty() && !allExistingBookmarks.isEmpty()) {
            productsToSaveOrUpdate.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            bookmarkRepository.saveAll(productsToSaveOrUpdate);
        }
        if (!bookmarksToRemoveForThisUser.isEmpty()) {
            user.getBookmarkedProducts().removeAll(bookmarksToRemoveForThisUser);
            userRepository.save(user);
        }

        if (!productsInCommon.isEmpty()) {
            for (BookmarkedProduct bookmarkedProduct : productsInCommon) {
                user.getBookmarkedProducts().add(bookmarkedProduct);
            }
            userRepository.save(user);
        }
        var bookmarkWithoutUser = bookmarkRepository.findBookmarkWithoutUser();
        bookmarkRepository.deleteAll(bookmarkWithoutUser);

        return bookmarkRepository.findMercadoLibreProductsByUserId(userId);
    }

    private List<BookmarkedProduct> getAllBookmarkedProducts(MercadoLibreAccessToken authorizationHeader) {
        WebClient webClient = authClientService.getWebClientWithAuthorizationHeader(authorizationHeader.getAccessToken());
        Flux<Bookmark> bookmarksFlux = fetchBookmarksFromApi(webClient);
        return fetchBookmarkedProductsByItemId(bookmarksFlux, webClient);
    }

    private Flux<Bookmark> fetchBookmarksFromApi(WebClient webClient) {
        String bookmarksUrl = "/users/me/bookmarks";
        return webClient.get()
                .uri(bookmarksUrl)
                .retrieve()
                .bodyToFlux(Bookmark.class);
    }

    private List<BookmarkedProduct> fetchBookmarkedProductsByItemId(Flux<Bookmark> bookmarksFlux, WebClient webClient) {
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
}
