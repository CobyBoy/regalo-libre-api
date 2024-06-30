package com.regalo_libre.mercadolibre.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkedProduct, Long> {
    List<BookmarkedProduct> findAllByIdIn(Collection<String> id);

    @Query("SELECT p FROM MercadoLibreUser u JOIN u.bookmarkedProducts p WHERE u.id = :userId ORDER BY p.bookmarkedDate DESC")
    List<BookmarkedProduct> findMercadoLibreProductsByUserId(@Param("userId") Long userId);

    @Query("SELECT B FROM BookmarkedProduct B WHERE B.users is EMPTY")
    List<BookmarkedProduct> findBookmarkWithoutUser();
}
