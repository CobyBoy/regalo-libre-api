package com.regalo_libre.mercadolibre.auth.service;

import com.regalo_libre.mercadolibre.auth.IMercadoLibreAuthClientService;
import com.regalo_libre.mercadolibre.auth.MercadoLibreConfig;
import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreAccessTokenRepository;
import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreUserRepository;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import com.regalo_libre.profile.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class MercadoLibreAuthClientServiceImpl implements IMercadoLibreAuthClientService {
    public static final String ERROR_FETCHING = "Error fetching";
    private final MercadoLibreAccessTokenRepository mercadoLibreAccessTokenRepository;
    private final MercadoLibreUserRepository mercadoLibreUserRepository;
    private final ProfileRepository profileRepository;
    private final MercadoLibreConfig mercadoLibreConfig;

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

/*    private MercadoLibreUserDTO saveAccessToken(MercadoLibreAccessToken accessToken) {
        Optional<MercadoLibreUser> optionalUser = mercadoLibreUserRepository.findById(accessToken.getUserId());
        MercadoLibreUser user;
        if (optionalUser.isEmpty()) {
            WebClient webClient = getWebClientWithAuthorizationHeader(accessToken.getAccessToken());
            user = getUserInfoFromApi(webClient);
            user.setAccessToken(accessToken);
            accessToken.setMercadoLibreUser(user);
        } else {
            user = optionalUser.get();
            Optional<MercadoLibreAccessToken> token = mercadoLibreAccessTokenRepository.findById(accessToken.getUserId());
            token.ifPresent(
                    mercadoLibreAccessToken ->
                            mercadoLibreAccessToken.setAccessToken(accessToken.getAccessToken())
            );
        }
        return MercadoLibreUserDTO.builder().build().toDto(mercadoLibreUserRepository.save(user));
    }*/

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

