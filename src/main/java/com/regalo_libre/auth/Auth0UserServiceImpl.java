package com.regalo_libre.auth;

import com.regalo_libre.auth.config.Auth0PropertiesConfig;
import com.regalo_libre.auth.model.Auth0User;
import com.regalo_libre.auth.repository.Auth0UserRepository;
import com.regalo_libre.mercadolibre.auth.exception.UserNotFoundException;
import com.regalo_libre.profile.Profile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class Auth0UserServiceImpl implements Auth0UserService {
    private final Auth0UserRepository repository;
    private final Auth0PropertiesConfig auth0PropertiesConfig;

    public Auth0UserInfo getAuth0UserInfo(String authorizationHeader) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        Auth0UserInfo userInfo;
        try {
            userInfo = restTemplate.exchange(
                    auth0PropertiesConfig.getUserInfoUrl(),
                    HttpMethod.GET,
                    entity,
                    Auth0UserInfo.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            throw e;
        }
        log.info("Getting user profile {}", userInfo);
        return userInfo;
    }

    @Transactional
    public Auth0User createAuth0User(Auth0UserInfo userInfo) {
        Auth0User user = buildUser(userInfo);
        log.info("Creating user profile for user {}", user);
        Optional<Auth0User> userFound = repository.findById(user.getId());
        if (userFound.isEmpty()) {
            user.setProfile(buildProfile(userInfo));
            return repository.save(user);
        }
        return userFound.get();
    }

    private Auth0User buildUser(Auth0UserInfo userInfo) {
        return Auth0User.builder()
                .fullStringId(userInfo.sub)
                .sub(userInfo.sub)
                .name(userInfo.name)
                .nickname(userInfo.nickname)
                .pictureUrl(userInfo.pictureUrl.replaceAll("\\.(jpg|jpeg|png)$", ".webp"))
                .updatedAt(userInfo.updatedAt)
                .build();
    }

    private Profile buildProfile(Auth0UserInfo userInfo) {
        return Profile.builder()
                .biography("")
                .isPrivate(true)
                .meliNickname(userInfo.nickname)
                .appNickname(userInfo.nickname.replace(" ", "_").toLowerCase())
                .pictureUrl(userInfo.pictureUrl.replaceAll("\\.(jpg|jpeg|png)$", ".webp"))
                .name(userInfo.name)
                .build();
    }

    public Auth0User getAuth0UserById(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    public Boolean existsById(Long userId) {
        return repository.existsById(userId);
    }

    public Auth0User findAuth0UserById(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public void saveAuth0User(Auth0User user) {
        repository.save(user);
    }

    @Transactional
    public Optional<Auth0User> findByProfileId(Long profileId) {
        return repository.findByProfile_ProfileId(profileId);
    }
}
