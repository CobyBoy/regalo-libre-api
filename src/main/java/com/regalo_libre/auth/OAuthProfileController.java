package com.regalo_libre.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class OAuthProfileController {
    private final OauthPropertiesConfig oauthPropertiesConfig;

    @GetMapping("/api/v1/oauth-profile")
    public ResponseEntity getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        OAuthUserInfo response = restTemplate.exchange(
                oauthPropertiesConfig.getUserInfoUrl(),
                HttpMethod.GET,
                entity,
                OAuthUserInfo.class
        ).getBody();

        return ResponseEntity.ok(response);
    }
}
