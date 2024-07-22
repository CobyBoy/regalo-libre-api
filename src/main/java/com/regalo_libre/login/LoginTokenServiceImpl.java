package com.regalo_libre.login;

import com.regalo_libre.auth.config.OauthPropertiesConfig;
import com.regalo_libre.auth.jwt.OAuthAccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginTokenServiceImpl {
    private final OauthPropertiesConfig oauthPropertiesConfig;

    public OAuthAccessToken getOAuthToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", oauthPropertiesConfig.getClientId());
        map.add("client_secret", oauthPropertiesConfig.getClientSecret());
        map.add("code", code);
        map.add("redirect_uri", "https://localhost:4200");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        OAuthAccessToken response;
        try {
            response = restTemplate.postForEntity(
                    oauthPropertiesConfig.getLoginTokenUrl(),
                    request,
                    OAuthAccessToken.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            throw e;
        }
        log.info("Returning oauth token {}", response);
        return response;
    }
}
