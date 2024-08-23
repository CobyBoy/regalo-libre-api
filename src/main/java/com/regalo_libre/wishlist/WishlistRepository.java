package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.model.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {
    @Query("SELECT DISTINCT w FROM WishList w LEFT JOIN FETCH w.gifts LEFT JOIN FETCH w.user u LEFT JOIN FETCH u.profile WHERE w.user.id = :auth0UserId ORDER BY w.createdAt DESC")
    Page<WishList> findAllByUserId(Long auth0UserId, Pageable pageable);

    List<WishList> findAllByUserIdOrderByCreatedAtDesc(Long auth0UserId);

    WishList findByPublicIdAndIsPrivateFalse(String id);

    List<WishList> findAllByUserIdAndIsPrivateFalseOrderByCreatedAtDesc(Long auth0UserId);

    @Query("SELECT w FROM WishList w JOIN w.user u WHERE u.profile.appNickname = :nickname AND w.isPrivate = false AND u.profile.isPrivate = false ORDER BY w.createdAt DESC")
    List<WishList> findPublicWishlistForPublicProfile(String nickname);


    WishList findByNameAndUserId(String name, Long auth0UserId);
}
