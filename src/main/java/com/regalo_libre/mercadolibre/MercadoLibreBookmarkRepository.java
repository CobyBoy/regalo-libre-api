package com.regalo_libre.mercadolibre;

import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MercadoLibreBookmarkRepository extends JpaRepository<BookmarkedProduct, Long> {
    List<BookmarkedProduct> findAllByIdIn(Iterable<String> ids);

    @Query("SELECT p FROM MercadoLibreUser u JOIN u.bookmarkedProducts p WHERE u.id = :userId ORDER BY p.bookmarkedDate DESC")
    List<BookmarkedProduct> findMercadoLibreProductsByUserId(@Param("userId") Long userId);
}