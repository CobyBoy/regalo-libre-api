package com.regalo_libre.auth.repository;

import com.regalo_libre.auth.model.Auth0User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Auth0UserRepository extends JpaRepository<Auth0User, Long> {
    Optional<Auth0User> findByProfile_ProfileId(Long profileId);
}
