package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUserId(Long oAuthUserId);

    WishList findByPublicIdAndIsPrivateFalse(String id);

    List<WishList> findAllByUserIdAndIsPrivateFalse(Long oAuthUserId);


    @Query("SELECT w FROM WishList w JOIN w.user u WHERE w.user.nickname = :nickname AND w.isPrivate = false AND u.profile.isPrivate = false")
    List<WishList> findPublicWishlistForPublicProfile(String nickname);
}
