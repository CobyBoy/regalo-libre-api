package com.regalo_libre.wishlist;

import com.regalo_libre.wishlist.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUserId(Long userId);

    WishList findByPublicIdAndIsPrivateFalse(String id);

    List<WishList> findAllByUserIdAndIsPrivateFalse(Long userId);

    List<WishList> findAllByUserNicknameAndIsPrivateFalse(String nickname);
}
