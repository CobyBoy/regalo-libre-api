package com.meli.wishlist.domain.mercadolibre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MercadoLibreProductRepo extends JpaRepository<MercadoLibreProduct, String > {
    List<MercadoLibreProduct> findAllByIdIn(Iterable<String> ids);
}
