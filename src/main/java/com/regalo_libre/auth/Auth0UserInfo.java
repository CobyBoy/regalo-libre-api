package com.regalo_libre.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class Auth0UserInfo {
    @JsonProperty("name")
    String name;
    @JsonProperty("nickname")
    String nickname;
    @JsonProperty("picture")
    String pictureUrl;
    @JsonProperty("sub")
    String sub;
    @JsonProperty("updated_at")
    String updatedAt;
}
