package com.meli.wishlist.domain.mercadolibre.favorites;

import com.meli.wishlist.domain.mercadolibre.MercadoLibreProduct;
import com.meli.wishlist.domain.mercadolibre.MercadoLibreProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesService {
    private final MercadoLibreProductRepo mercadoLibreProductRepo;

    public List<MercadoLibreProduct> getAllFavorites() {
        return mercadoLibreProductRepo.findAll();
    }
}
