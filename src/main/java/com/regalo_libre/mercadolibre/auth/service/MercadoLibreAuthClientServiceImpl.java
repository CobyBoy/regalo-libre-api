package com.regalo_libre.mercadolibre.auth.service;

import com.regalo_libre.mercadolibre.auth.IMercadoLibreAuthClientService;
import com.regalo_libre.mercadolibre.auth.MercadoLibreConfig;
import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreAccessTokenRepository;
import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreUserRepository;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import com.regalo_libre.profile.Profile;
import com.regalo_libre.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MercadoLibreAuthClientServiceImpl implements IMercadoLibreAuthClientService {
    public static final String ERROR_FETCHING = "Error fetching";
    private final MercadoLibreAccessTokenRepository mercadoLibreAccessTokenRepository;
    private final MercadoLibreUserRepository mercadoLibreUserRepository;
    private final ProfileRepository profileRepository;
    private final MercadoLibreConfig mercadoLibreConfig;

    public MercadoLibreUser getMercadoLibreUserData(String authorizationCode) {
        WebClient webClient = WebClient.create();
        MercadoLibreAccessToken accessToken = getAccessToken(webClient, authorizationCode);
        return saveAccessToken(accessToken);
    }

    private MercadoLibreAccessToken getAccessToken(WebClient webClient, String authorizationCode) {
        return webClient.post()
                .uri(mercadoLibreConfig.getApiUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(
                                "grant_type", "authorization_code")
                        .with("client_id", mercadoLibreConfig.getClientId())
                        .with("client_secret", mercadoLibreConfig.getClientSecret())
                        .with("code", authorizationCode)
                        .with("redirect_uri", mercadoLibreConfig.getRedirectUri()))
                .retrieve()
                .bodyToMono(MercadoLibreAccessToken.class)
                .block();
    }

    private MercadoLibreUser saveAccessToken(MercadoLibreAccessToken accessToken) {
        Optional<MercadoLibreUser> optionalUser = mercadoLibreUserRepository.findById(accessToken.getUserId());
        MercadoLibreUser user;
        if (optionalUser.isEmpty()) {
            WebClient webClient = getWebClientWithAuthorizationHeader(accessToken.getAccessToken());
            user = getUserInfoFromApi(webClient);
            user.setAccessToken(accessToken);
            accessToken.setMercadoLibreUser(user);
            var profile = Profile.builder().mercadoLibreUser(user)
                    .meliNickname(user.getNickname())
                    .appNickname(user.getNickname())
                    .isPrivate(true)
                    .biography("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum")
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .pictureUrl(user.getThumbnail().getPictureUrl())
                    .build();
            user.setProfile(profile);
            profile.setMercadoLibreUser(user);
            profileRepository.save(profile);
        } else {
            user = optionalUser.get();
            Optional<MercadoLibreAccessToken> token = mercadoLibreAccessTokenRepository.findById(accessToken.getUserId());
            token.ifPresent(
                    mercadoLibreAccessToken ->
                            mercadoLibreAccessToken.setAccessToken(accessToken.getAccessToken())
            );
        }
        mercadoLibreUserRepository.save(user);
        return user;
    }

    private MercadoLibreUser getUserInfoFromApi(WebClient webClient) {
        String userUrl = "/users/me";
        try {
            return webClient.get()
                    .uri(userUrl)
                    .retrieve()
                    .bodyToMono(MercadoLibreUser.class)
                    .block();
        } catch (Exception ex) {
            log.error(ERROR_FETCHING);
            throw new RestClientException(ERROR_FETCHING);
        }

    }

    public WebClient getWebClientWithAuthorizationHeader(String authorizationHeader) {
        String baseUrl = "https://api.mercadolibre.com";
        try {
            return WebClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authorizationHeader)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .build();
        } catch (Exception ex) {
            log.error(ERROR_FETCHING);
            throw new RestClientException(ERROR_FETCHING);
        }
    }

}

