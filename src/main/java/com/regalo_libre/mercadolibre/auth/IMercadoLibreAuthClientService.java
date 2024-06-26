package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import org.springframework.web.reactive.function.client.WebClient;

public interface IMercadoLibreAuthClientService {
    MercadoLibreUser getMercadoLibreUserData(String authorizationCode);

    WebClient getWebClientWithAuthorizationHeader(String authorizationHeader);
}
