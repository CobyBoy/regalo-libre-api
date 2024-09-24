package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.dto.DashboardWishlistDto;
import com.regalo_libre.wishlist.dto.PublicProfileWishlistDto;
import com.regalo_libre.wishlist.dto.PublicWishlistDto;
import com.regalo_libre.wishlist.model.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {

    @Query("SELECT new com.regalo_libre.wishlist.dto.DashboardWishlistDto(w.name, SIZE(w.gifts), w.wishlistId, w.publicId, w.isPrivate, w.user.profile.appNickname) " +
            "FROM WishList w " +
            "WHERE w.user.id = :auth0UserId " +
            "ORDER BY w.createdAt DESC")
    Page<DashboardWishlistDto> findAllWishlistForDashboardByUserId(Long auth0UserId, Pageable pageable);

    List<WishList> findAllByUserIdOrderByCreatedAtDesc(Long auth0UserId);

    @Query("SELECT w FROM WishList w WHERE w.user.profile.appNickname =:nickname AND w.wishlistId =:id AND w.isPrivate = false ")
    WishList findByWishlistIdAndIsPrivateFalse(Long id, String nickname);

    @Query("SELECT new com.regalo_libre.wishlist.dto.PublicWishlistDto(w.wishlistId, w.name, SIZE(w.gifts)) " +
            "FROM WishList w " +
            "WHERE w.user.id =:auth0UserId AND w.isPrivate = false " +
            "ORDER BY w.createdAt DESC")
    Page<PublicWishlistDto> findAllByUserIdAndIsPrivateFalseOrderByCreatedAtDesc(Long auth0UserId, Pageable pageable);

    @Query("SELECT new com.regalo_libre.wishlist.dto.PublicProfileWishlistDto(w.wishlistId, w.name, w.publicId, SIZE(w.gifts)) FROM WishList w " +
            "JOIN w.user u " +
            "WHERE u.profile.appNickname = :nickname AND w.isPrivate = false AND u.profile.isPrivate = false " +
            "ORDER BY w.createdAt DESC")
    Page<PublicProfileWishlistDto> findPublicWishlistForPublicProfile(String nickname, Pageable pageable);

    WishList findByNameAndUserId(String name, Long auth0UserId);

}
