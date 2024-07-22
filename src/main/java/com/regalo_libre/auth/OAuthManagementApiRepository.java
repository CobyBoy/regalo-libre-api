package com.regalo_libre.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthManagementApiRepository extends JpaRepository<OAuthManagementApiToken, Long> {
}
