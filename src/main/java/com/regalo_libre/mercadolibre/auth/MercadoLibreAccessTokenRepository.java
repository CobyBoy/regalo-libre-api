package com.regalo_libre.mercadolibre.auth;


import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MercadoLibreAccessTokenRepository extends JpaRepository<MercadoLibreAccessToken, Long> {
    @Override
    Optional<MercadoLibreAccessToken> findById(Long integer);
}
