package com.meli.wishlist.domain.mercadolibre.auth;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoLibreAuthRepo extends JpaRepository<MercadoLibreAccessToken, Integer> {
}
