package com.regalo_libre.favorites;

import com.regalo_libre.mercadolibre.auth.MercadoLibreUser;
import com.regalo_libre.mercadolibre.bookmark.Bookmark;
import com.regalo_libre.mercadolibre.bookmark.BookmarkItem;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.mercadolibre.MercadoLibreBookmarkRepository;
import com.regalo_libre.mercadolibre.auth.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.auth.MercadoLibreAccessTokenRepository;
import com.regalo_libre.mercadolibre.auth.MercadoLibreAuthClientServiceImpl;
import com.regalo_libre.mercadolibre.auth.MercadoLibreUserRepository;
import com.regalo_libre.wishlist.exception.TokenNotFoundException;
import com.regalo_libre.wishlist.exception.UserNotFoundException;
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
public class FavoritesService {
    private final MercadoLibreBookmarkRepository mercadoLibreBookmarkRepo;
    private final MercadoLibreAccessTokenRepository mercadoLibreAccessTokenRepository;
    private final MercadoLibreAuthClientServiceImpl mercadoLibreAuthClientServiceImpl;
    private final MercadoLibreUserRepository mercadoLibreUserRepository;

    public List<BookmarkedProduct> getAllFavorites(Long userId) {
        var token = mercadoLibreAccessTokenRepository.findById(userId).orElseThrow(() -> new TokenNotFoundException("Sesion no encontrada"));
        MercadoLibreUser user = mercadoLibreUserRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        var apiProducts = getAllBookmarkedProducts(token);
        var existingBookmarkedProducts = mercadoLibreBookmarkRepo.findMercadoLibreProductsByUserId(userId);
        if (apiProducts.isEmpty() && existingBookmarkedProducts.isEmpty()) {
            return new ArrayList<>();
        }
        if (existingBookmarkedProducts.isEmpty()) {
            apiProducts.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            mercadoLibreBookmarkRepo.saveAll(apiProducts);
        }
        Map<String, BookmarkedProduct> existingProductMap = existingBookmarkedProducts.stream()
                .collect(Collectors.toMap(BookmarkedProduct::getId, product -> product));
        Map<String, BookmarkedProduct> apiProductMap = apiProducts.stream()
                .collect(Collectors.toMap(BookmarkedProduct::getId, product -> product));

        List<BookmarkedProduct> productsToSaveOrUpdate = new ArrayList<>();
        for (BookmarkedProduct apiProduct : apiProducts) {
            BookmarkedProduct existingProduct = existingProductMap.get(apiProduct.getId());
            if (existingProduct == null) {
                // New product
                productsToSaveOrUpdate.add(apiProduct);
            }
        }

        List<BookmarkedProduct> productsToRemove = new ArrayList<>();
        for (BookmarkedProduct bookmarkedProduct : existingBookmarkedProducts) {
            BookmarkedProduct existingProduct = apiProductMap.get(bookmarkedProduct.getId());
            if (existingProduct == null) {
                // Product to remove
                productsToRemove.add(existingProductMap.get(bookmarkedProduct.getId()));
            }
        }

        if (!productsToSaveOrUpdate.isEmpty() && !existingBookmarkedProducts.isEmpty()) {
            productsToSaveOrUpdate.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            mercadoLibreBookmarkRepo.saveAll(productsToSaveOrUpdate);
        }
        if (!productsToRemove.isEmpty()) {
            //mercadoLibreProductRepo.deleteAll(productsToRemove);
            // existingBookmarkedProducts.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            user.getBookmarkedProducts().removeAll(productsToRemove);
            existingBookmarkedProducts.removeAll(productsToRemove);
            mercadoLibreBookmarkRepo.deleteAll(productsToRemove);
            //mercadoLibreProductRepo.saveAll(existingBookmarkedProducts);
        }


        return mercadoLibreBookmarkRepo.findMercadoLibreProductsByUserId(userId);
    }

    private void saveUserBookmarks(MercadoLibreAccessToken authorizationHeader, Long userId) {
        WebClient webClient = mercadoLibreAuthClientServiceImpl.getWebClientWithAuthorizationHeader(authorizationHeader.getAccessToken());
        Flux<Bookmark> bookmarksFlux = fetchBookmarksFromApi(webClient);
        List<BookmarkedProduct> products = fetchBookmarkedProducts(bookmarksFlux, webClient);
        MercadoLibreUser user = mercadoLibreUserRepository.findById(userId).orElseThrow();
        products.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
        mercadoLibreBookmarkRepo.saveAll(products);
    }

    private List<BookmarkedProduct> getAllBookmarkedProducts(MercadoLibreAccessToken authorizationHeader) {
        WebClient webClient = mercadoLibreAuthClientServiceImpl.getWebClientWithAuthorizationHeader(authorizationHeader.getAccessToken());
        Flux<Bookmark> bookmarksFlux = fetchBookmarksFromApi(webClient);
        return fetchBookmarkedProducts(bookmarksFlux, webClient);
    }

    private Flux<Bookmark> fetchBookmarksFromApi(WebClient webClient) {
        String bookmarksUrl = "/users/me/bookmarks";
        return webClient.get()
                .uri(bookmarksUrl)
                .retrieve()
                .bodyToFlux(Bookmark.class);
    }

    private List<BookmarkedProduct> fetchBookmarkedProducts(Flux<Bookmark> bookmarksFlux, WebClient webClient) {
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
