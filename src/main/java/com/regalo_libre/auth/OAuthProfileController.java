package com.regalo_libre.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthProfileController {
    private final OauthPropertiesConfig oauthPropertiesConfig;
    private final OAuthUserServiceImpl oAuthUserService;

    @GetMapping("/api/v1/oauth-profile")
    public ResponseEntity<OAuthUserInfoDTO> getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        OAuthUserInfo response;
        try {
            response = restTemplate.exchange(
                    oauthPropertiesConfig.getUserInfoUrl(),
                    HttpMethod.GET,
                    entity,
                    OAuthUserInfo.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            throw e;
        }


        log.info("Getting user profile {}", response);

        return ResponseEntity.ok(
                OAuthUserInfoDTO.builder()
                        .build()
                        .toDto(oAuthUserService.createOauthUser(response))
        );
    }
}
