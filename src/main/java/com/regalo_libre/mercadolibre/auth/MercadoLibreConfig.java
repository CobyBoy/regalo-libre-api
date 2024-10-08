package com.regalo_libre.mercadolibre.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MercadoLibreConfig {
    @Value("${meli.client_secret}")
    private String clientSecret;
    @Value("${meli.token_url}")
    private String tokenUrl;
    @Value("${meli.client_id}")
    private String clientId;
    @Value("${meli.redirect_uri}")
    private String redirectUri;
}
