package com.regalo_libre.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p WHERE p.appNickname  = :username AND p.isPrivate = false")
    Optional<Profile> findByAppNicknameAndIsPrivateFalse(@Param("username") String username);

    @Query("SELECT p FROM Profile p WHERE p.appNickname  = :username")
    Optional<Profile> findByAppNickname(@Param("username") String username);

    @Query("SELECT p FROM Profile p WHERE p.profileId  = :profileId AND p.isPrivate = false")
    Optional<Profile> findByProfileIdAndIsPrivateFalse(Long profileId);
}
