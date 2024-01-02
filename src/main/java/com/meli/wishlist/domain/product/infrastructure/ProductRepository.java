package com.meli.wishlist.domain.product.infrastructure;

import com.meli.wishlist.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ProductRepository extends JpaRepository<Product, String> {
}
