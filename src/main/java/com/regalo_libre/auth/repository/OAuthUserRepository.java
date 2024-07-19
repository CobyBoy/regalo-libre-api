package com.regalo_libre.auth.repository;

import com.regalo_libre.auth.model.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthUserRepository extends JpaRepository<OAuthUser, String> {
}
