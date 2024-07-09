package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUserDTO;
import org.springframework.web.reactive.function.client.WebClient;

public interface IMercadoLibreAuthClientService {
    MercadoLibreUserDTO getMercadoLibreUserData(String authorizationCode);

    WebClient getWebClientWithAuthorizationHeader(String authorizationHeader);
}
