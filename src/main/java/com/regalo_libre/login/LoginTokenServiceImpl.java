package com.regalo_libre.login;

import com.regalo_libre.auth.config.Auth0PropertiesConfig;
import com.regalo_libre.auth.jwt.Auth0AccessToken;
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
    private final Auth0PropertiesConfig auth0PropertiesConfig;

    public Auth0AccessToken getAuth0Token(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", auth0PropertiesConfig.getClientId());
        map.add("client_secret", auth0PropertiesConfig.getClientSecret());
        map.add("code", code);
        map.add("redirect_uri", "https://192.168.0.37:4200");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        Auth0AccessToken response;
        try {
            response = restTemplate.postForEntity(
                    auth0PropertiesConfig.getLoginTokenUrl(),
                    request,
                    Auth0AccessToken.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            throw e;
        }
        log.info("Returning auth0 token {}", response);
        return response;
    }
}
