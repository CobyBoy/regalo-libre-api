package com.meli.wishlist.domain.product;

import com.meli.wishlist.domain.product.infrastructure.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    @Transactional
    public void saveMeliProducts(List<Product> productList) {
        productRepository.saveAll(productList);
    }
}
