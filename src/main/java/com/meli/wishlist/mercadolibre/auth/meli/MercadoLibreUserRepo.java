package com.meli.wishlist.mercadolibre.auth.meli;

import com.meli.wishlist.mercadolibre.auth.meli.model.MercadoLibreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MercadoLibreUserRepo extends JpaRepository<MercadoLibreUser, Long> {
}
