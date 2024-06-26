package com.meli.wishlist.domain.mercadolibre.auth.meli;

import com.meli.wishlist.domain.mercadolibre.auth.meli.model.MercadoLibreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MercadoLibreUserRepo extends JpaRepository<MercadoLibreUser, Long> {
}
