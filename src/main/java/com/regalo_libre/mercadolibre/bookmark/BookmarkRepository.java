package com.regalo_libre.mercadolibre.bookmark;

import com.regalo_libre.bookmarks.dto.BookmarkDTO;
import com.regalo_libre.bookmarks.dto.BookmarkDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkedProduct, Long> {
    List<BookmarkedProduct> findAllByIdIn(Collection<String> id);

    @Query("SELECT p FROM Auth0User u JOIN u.bookmarkedProducts p WHERE u.id = :userId ORDER BY p.bookmarkedDate DESC")
    Page<BookmarkDTO> findMercadoLibreProductsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT B FROM BookmarkedProduct B WHERE B.users is EMPTY")
    List<BookmarkedProduct> findBookmarkWithoutUser();

    @Query("SELECT new com.regalo_libre.bookmarks.dto.BookmarkDetailsDto(b.currencyId, b.id, b.permalink, b.price, b.status, b.thumbnail, b.title) " +
            "FROM BookmarkedProduct b " +
            "JOIN b.wishLists w " +
            "WHERE w.wishlistId= :wishlistId")
    Page<BookmarkDetailsDto> findGiftsByWishlistId(@Param("wishlistId") Long wishlistId, Pageable pageable);
}
