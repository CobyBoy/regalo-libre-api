package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.bookmarks.events.OnMercadoLibreAccessTokenRetrievedPublisher;
import com.regalo_libre.mercadolibre.auth.exception.TokenNotFoundException;
import com.regalo_libre.mercadolibre.auth.exception.UnableToSaveMercadoLibreAccessTokenException;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreAccessTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoLibreAccessTokenServiceImpl implements MercadoLibreAccessTokenService {
    private final MercadoLibreAccessTokenRepository mercadoLibreAccessTokenRepository;
    private final MercadoLibreConfig mercadoLibreConfig;
    public static final String ERROR_FETCHING = "Error fetching";
    private final OnMercadoLibreAccessTokenRetrievedPublisher eventPublisher;

    public void exchangeCodeForToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", mercadoLibreConfig.getClientId());
        map.add("client_secret", mercadoLibreConfig.getClientSecret());
        map.add("code", authorizationCode);
        map.add("redirect_uri", mercadoLibreConfig.getRedirectUri());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        log.info("Getting Mercado Libre token with {}", map);

        ResponseEntity<MercadoLibreAccessToken> response = restTemplate.postForEntity(mercadoLibreConfig.getTokenUrl(), request, MercadoLibreAccessToken.class);
        if (response.getBody() != null) {
            var token = saveAccessToken(response.getBody());
            log.info("Token get in thread {}", Thread.currentThread().getName());
            eventPublisher.publishEvent(token.getUserId());
        } else {
            throw new UnableToSaveMercadoLibreAccessTokenException("Algo falló con Mercado Libre. Intentá loguearte de nuevo.");
        }

    }

    public WebClient getWebClientWithABearerToken(String token) {
        String baseUrl = "https://api.mercadolibre.com";
        try {
            return WebClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .build();
        } catch (Exception ex) {
            log.error(ERROR_FETCHING);
            throw new RestClientException(ERROR_FETCHING);
        }
    }

    @Transactional
    private MercadoLibreAccessToken saveAccessToken(MercadoLibreAccessToken accessToken) {
        log.info("Saving Mercado Libre access token");
        accessToken.setExpiresAt(LocalDateTime.now().plusSeconds(accessToken.getExpiresIn()));
        return mercadoLibreAccessTokenRepository.save(accessToken);
    }

    public MercadoLibreAccessToken getMercadoLibreAccessToken(Long userId) {
        var token = mercadoLibreAccessTokenRepository.findById(userId).orElseThrow(() -> new TokenNotFoundException("El token de Mercado Libre no existe"));
        if (isMercadoLibreAccessTokenExpired(token)) {
            return refreshToken(token.getRefreshToken());
        } else return token;

    }

    private boolean isMercadoLibreAccessTokenExpired(MercadoLibreAccessToken token) {
        return LocalDateTime.now().isAfter(token.getExpiresAt());
    }

    private MercadoLibreAccessToken refreshToken(String refreshToken) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", mercadoLibreConfig.getClientId());
        params.add("client_secret", mercadoLibreConfig.getClientSecret());
        params.add("refresh_token", refreshToken);

        log.info("Refreshing Mercado Libre token with {}", params);

        ResponseEntity<MercadoLibreAccessToken> response = restTemplate.postForEntity(mercadoLibreConfig.getTokenUrl(), params, MercadoLibreAccessToken.class);
        MercadoLibreAccessToken newToken = response.getBody();
        assert newToken != null;
        saveAccessToken(newToken);

        return newToken;
    }
}
