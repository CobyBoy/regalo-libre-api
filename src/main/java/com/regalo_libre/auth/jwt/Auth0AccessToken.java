package com.regalo_libre.auth.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Auth0AccessToken {
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("id_token")
    String idToken;
    @JsonProperty("scope")
    String scope;
    @JsonProperty("expires_in")
    String expiresIn;
    @JsonProperty("token_type")
    String tokenType;
}
