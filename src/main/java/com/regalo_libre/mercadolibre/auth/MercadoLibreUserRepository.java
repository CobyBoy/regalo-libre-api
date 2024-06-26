package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MercadoLibreUserRepository extends JpaRepository<MercadoLibreUser, Long> {
}
