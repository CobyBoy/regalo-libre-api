package com.meli.wishlist.mercadolibre.auth.meli;

import com.meli.wishlist.mercadolibre.auth.meli.model.MercadoLibreAccessToken;
import com.meli.wishlist.mercadolibre.auth.meli.model.MercadoLibreUser;
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
public class MercadoLibreAuthClient {
    private final MercadoLibreAccessTokenRepo mercadoLibreAccessTokenRepo;
    private final MercadoLibreUserRepo mercadoLibreUserRepo;

    public MercadoLibreUser getMercadoLibreUserData(String authorizationCode) {
        WebClient webClient = WebClient.create();
        MercadoLibreAccessToken accessToken = getAccessToken(webClient, authorizationCode);
        var user = saveAccessToken(accessToken);
        return user;
        //return saveUserInfo(accessToken);
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
        var optionalUser = mercadoLibreUserRepo.findById(accessToken.getUserId());
        MercadoLibreUser user;
        if (optionalUser.isEmpty()) {
            WebClient webClient = getWebClientWithAuthorizationHeader(accessToken.getAccessToken());
            user = getUserInfoFromApi(webClient);
            mercadoLibreUserRepo.save(user);
        } else {
            user = optionalUser.get();
            var token = mercadoLibreAccessTokenRepo.findById(accessToken.getUserId());
            if (token.isPresent()) {
                token.get().setAccessToken(accessToken.getAccessToken());
            }
        }
        mercadoLibreAccessTokenRepo.save(accessToken);
        return user;
    }

   /* private MercadoLibreUser saveUserInfo(MercadoLibreAccessToken accessToken) {
        WebClient webClient = getWebClientWithAuthorizationHeader(accessToken.getAccessToken());
        MercadoLibreUser userInfo = getUserInfoFromApi(webClient);
        log.info("Saving mercado libre user" + userInfo.toString());
        return mercadoLibreUserRepo.save(userInfo);

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
            log.error("Error fetching");
            throw new RestClientException("Error fetching");
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
            log.error("Error fetching");
            throw new RestClientException("Error fetching");
        }
    }

}

