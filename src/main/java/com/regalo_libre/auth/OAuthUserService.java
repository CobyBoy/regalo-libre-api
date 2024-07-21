package com.regalo_libre.auth;

import com.regalo_libre.auth.model.OAuthUser;

public interface OAuthUserService {
    OAuthUserInfo getOauthUserInfo(String authorizationHeader);

    OAuthUser getOAuthUserById(Long userId);

    OAuthUser createOauthUser(OAuthUserInfo response);
}
