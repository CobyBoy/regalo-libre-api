package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
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
public class IMercadoLibreAuthClientServiceImpl implements IMercadoLibreAuthClientService {
    public static final String ERROR_FETCHING = "Error fetching";
    private final MercadoLibreAccessTokenRepository mercadoLibreAccessTokenRepository;
    private final MercadoLibreUserRepository mercadoLibreUserRepository;

    public MercadoLibreUser getMercadoLibreUserData(String authorizationCode) {
        WebClient webClient = WebClient.create();
        MercadoLibreAccessToken accessToken = getAccessToken(webClient, authorizationCode);
        return saveAccessToken(accessToken);
    }

    private MercadoLibreAccessToken getAccessToken(WebClient webClient, String authorizationCode) {
        String apiUrl = "https://api.mercadolibre.com/oauth/token";
        String clientId = "3828299958180754";
        String clientSecret = "4SQfIVwKR0YlVhT4NmiqbwtIQRSaCYTK";
        String redirectUri = "https://localhost:4200/code";
        return webClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(
                                "grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("code", authorizationCode)
                        .with("redirect_uri", redirectUri))
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

