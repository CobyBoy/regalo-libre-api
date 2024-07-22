package com.regalo_libre.auth;

import com.regalo_libre.auth.config.OauthPropertiesConfig;
import com.regalo_libre.auth.model.OAuthUser;
import com.regalo_libre.auth.repository.OAuthUserRepository;
import com.regalo_libre.profile.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthUserServiceImpl implements OAuthUserService {
    private final OAuthUserRepository repository;
    private final OauthPropertiesConfig oauthPropertiesConfig;
    private final ApplicationEventPublisher eventPublisher;

    public OAuthUserInfo getOauthUserInfo(String authorizationHeader) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        OAuthUserInfo userInfo;
        try {
            userInfo = restTemplate.exchange(
                    oauthPropertiesConfig.getUserInfoUrl(),
                    HttpMethod.GET,
                    entity,
                    OAuthUserInfo.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            throw e;
        }
        log.info("Getting user profile {}", userInfo);
        return userInfo;
    }

    public OAuthUser createOauthUser(OAuthUserInfo userInfo) {
        OAuthUser user = buildUser(userInfo);
        Optional<OAuthUser> userFound = repository.findById(user.getId());
        if (userFound.isEmpty()) {
            user.setProfile(buildProfile(userInfo));
            eventPublisher.publishEvent(new OAuthUserCreatedEvent(this, user));
            return repository.save(user);
        }
        return userFound.get();
    }

    private OAuthUser buildUser(OAuthUserInfo userInfo) {
        return OAuthUser.builder()
                .fullStringId(userInfo.sub)
                .sub(userInfo.sub)
                .name(userInfo.name)
                .nickname(userInfo.nickname)
                .pictureUrl(userInfo.pictureUrl)
                .updatedAt(userInfo.updatedAt)
                .build();
    }

    private Profile buildProfile(OAuthUserInfo userInfo) {
        return Profile.builder()
                .biography("biography")
                .isPrivate(true)
                .meliNickname(userInfo.nickname)
                .appNickname(userInfo.nickname)
                .pictureUrl(userInfo.pictureUrl)
                .name(userInfo.name)
                .build();
    }

    public OAuthUser getOAuthUserById(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

}
