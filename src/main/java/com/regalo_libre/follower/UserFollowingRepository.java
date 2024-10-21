package com.regalo_libre.follower;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFollowingRepository extends JpaRepository<UserFollowing, UserFollowingId> {
    List<UserFollowing> findByFollowerId(Long followerId);

    List<UserFollowing> findByFolloweeId(Long followeeId);

    Optional<UserFollowing> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    @Query("SELECT new com.regalo_libre.follower.FollowerDTO(uf.follower.id, uf.follower.profile.appNickname, uf.followedDate, uf.follower.pictureUrl) " +
            "FROM UserFollowing uf WHERE uf.followee.id = :userId AND uf.isFollowing = true")
    List<FollowerDTO> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.regalo_libre.follower.FollowerDTO(uf.followee.id, uf.followee.profile.appNickname, uf.followedDate, uf.followee.pictureUrl) " +
            "FROM UserFollowing uf WHERE uf.follower.id = :userId AND uf.isFollowing = true")
    List<FollowerDTO> findFolloweesByUserId(@Param("userId") Long userId);
}
