package com.meli.wishlist.favorites;

import com.meli.wishlist.mercadolibre.auth.meli.model.MercadoLibreUser;
import com.meli.wishlist.mercadolibre.model.Bookmark;
import com.meli.wishlist.mercadolibre.model.BookmarkItem;
import com.meli.wishlist.mercadolibre.model.BookmarkedProduct;
import com.meli.wishlist.mercadolibre.MercadoLibreProductRepository;
import com.meli.wishlist.mercadolibre.auth.meli.model.MercadoLibreAccessToken;
import com.meli.wishlist.mercadolibre.auth.meli.MercadoLibreAccessTokenRepo;
import com.meli.wishlist.mercadolibre.auth.meli.MercadoLibreAuthClient;
import com.meli.wishlist.mercadolibre.auth.meli.MercadoLibreUserRepo;
import com.meli.wishlist.wishlist.exception.TokenNotFoundException;
import com.meli.wishlist.wishlist.exception.UserNotFoundException;
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
    private final MercadoLibreProductRepository mercadoLibreProductRepo;
    private final MercadoLibreAccessTokenRepo mercadoLibreAccessTokenRepo;
    private final MercadoLibreAuthClient mercadoLibreAuthClient;
    private final MercadoLibreUserRepo mercadoLibreUserRepo;

    public List<BookmarkedProduct> getAllFavorites(Long userId) {
        var token = mercadoLibreAccessTokenRepo.findById(userId).orElseThrow(() -> new TokenNotFoundException("Sesion no encontrada"));
        MercadoLibreUser user = mercadoLibreUserRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        var apiProducts = getAllBookmarkedProducts(token);
        var existingBookmarkedProducts = mercadoLibreProductRepo.findMercadoLibreProductsByUserId(userId);
        if (apiProducts.isEmpty() && existingBookmarkedProducts.isEmpty()) {
            return new ArrayList<>();
        }
        if (existingBookmarkedProducts.isEmpty()) {
            apiProducts.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            mercadoLibreProductRepo.saveAll(apiProducts);
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
            mercadoLibreProductRepo.saveAll(productsToSaveOrUpdate);
        }
        if (!productsToRemove.isEmpty()) {
            //mercadoLibreProductRepo.deleteAll(productsToRemove);
            // existingBookmarkedProducts.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
            user.getBookmarkedProducts().removeAll(productsToRemove);
            existingBookmarkedProducts.removeAll(productsToRemove);
            mercadoLibreProductRepo.deleteAll(productsToRemove);
            //mercadoLibreProductRepo.saveAll(existingBookmarkedProducts);
        }


        return mercadoLibreProductRepo.findMercadoLibreProductsByUserId(userId);
    }

    private void saveUserBookmarks(MercadoLibreAccessToken authorizationHeader, Long userId) {
        WebClient webClient = mercadoLibreAuthClient.getWebClientWithAuthorizationHeader(authorizationHeader.getAccessToken());
        Flux<Bookmark> bookmarksFlux = fetchBookmarksFromApi(webClient);
        List<BookmarkedProduct> products = fetchBookmarkedProducts(bookmarksFlux, webClient);
        MercadoLibreUser user = mercadoLibreUserRepo.findById(userId).orElseThrow();
        products.forEach(mercadoLibreProduct -> mercadoLibreProduct.setUsers(List.of(user)));
        mercadoLibreProductRepo.saveAll(products);
    }

    private List<BookmarkedProduct> getAllBookmarkedProducts(MercadoLibreAccessToken authorizationHeader) {
        WebClient webClient = mercadoLibreAuthClient.getWebClientWithAuthorizationHeader(authorizationHeader.getAccessToken());
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
