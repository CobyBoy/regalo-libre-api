package com.regalo_libre.auth;

import com.regalo_libre.auth.model.Auth0User;

import java.util.Optional;

public interface Auth0UserService {
    Auth0UserInfo getAuth0UserInfo(String authorizationHeader);

    Auth0User getAuth0UserById(Long userId);

    Auth0User createAuth0User(Auth0UserInfo response);

    Auth0User findAuth0UserById(Long userId);

    void saveAuth0User(Auth0User user);

    Boolean existsById(Long userId);

    Optional<Auth0User> findByProfileId(Long profileId);
}
