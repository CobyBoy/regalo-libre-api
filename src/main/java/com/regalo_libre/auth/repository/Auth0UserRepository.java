package com.regalo_libre.auth.repository;

import com.regalo_libre.auth.model.Auth0User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Auth0UserRepository extends JpaRepository<Auth0User, Long> {
}
