package com.regalo_libre.auth;

import com.regalo_libre.auth.model.OAuthUser;
import com.regalo_libre.auth.repository.OAuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthUserServiceImpl {
    private final OAuthUserRepository repository;
    private final OauthPropertiesConfig oauthPropertiesConfig;

    public OAuthUserInfo getOauthUserInfo(String authorizationHeader) {
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
        return response;
    }

    public OAuthUser createOauthUser(OAuthUserInfo response) {
        return repository.save(OAuthUser.builder()
                .fullStringId(response.sub)
                .name(response.name)
                .nickname(response.nickname)
                .picture(response.picture)
                .updatedAt(response.updatedAt)
                .build());
    }

}
