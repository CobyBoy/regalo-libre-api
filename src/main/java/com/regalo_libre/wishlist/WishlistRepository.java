package com.regalo_libre.wishlist;

import com.regalo_libre.auth.model.OAuthUser;
import com.regalo_libre.wishlist.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUser(OAuthUser oAuthUser);

    WishList findByPublicIdAndIsPrivateFalse(String id);

    List<WishList> findAllByUserAndIsPrivateFalse(OAuthUser oAuthUser);

    List<WishList> findAllByUserNicknameAndIsPrivateFalse(String nickname);
}
