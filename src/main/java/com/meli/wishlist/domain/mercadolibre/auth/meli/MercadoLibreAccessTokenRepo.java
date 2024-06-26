package com.meli.wishlist.domain.mercadolibre.auth.meli;


import com.meli.wishlist.domain.mercadolibre.auth.meli.model.MercadoLibreAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MercadoLibreAccessTokenRepo extends JpaRepository<MercadoLibreAccessToken, Long> {
    @Override
    Optional<MercadoLibreAccessToken> findById(Long integer);
}
