package com.regalo_libre.mercadolibre.auth;

import org.springframework.web.reactive.function.client.WebClient;

public interface IMercadoLibreAuthClientService {

    WebClient getWebClientWithAuthorizationHeader(String authorizationHeader);
}
