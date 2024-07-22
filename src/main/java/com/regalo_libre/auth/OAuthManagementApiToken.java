package com.regalo_libre.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
@Table(name = "token", schema = "oauth")
public class OAuthManagementApiToken {
    @Id
    private Long userId;
    @JsonProperty("access_token")
    @Column(length = 1127)
    private String accessToken;
    @Column(length = 300)
    private String scope;
    @JsonProperty("expires_in")
    private String expiresIn;
    @JsonProperty("token_type")
    private String tokenType;
    //access_token, scope,expires_in, token_type

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String access_token) {
        this.accessToken = access_token;
    }
}
