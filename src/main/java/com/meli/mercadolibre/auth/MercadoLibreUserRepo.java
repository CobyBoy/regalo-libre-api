package com.meli.mercadolibre.auth;

import com.meli.mercadolibre.auth.model.MercadoLibreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MercadoLibreUserRepo extends JpaRepository<MercadoLibreUser, Long> {
}
