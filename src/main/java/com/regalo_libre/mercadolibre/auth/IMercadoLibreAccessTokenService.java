package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import org.springframework.web.reactive.function.client.WebClient;

public interface IMercadoLibreAccessTokenService {
    void exchangeCodeForToken(String authorizationCode);

    WebClient getWebClientWithABearerToken(String token);

    MercadoLibreAccessToken getMercadoLibreAccessToken(Long userId);

}
