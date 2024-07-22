package com.regalo_libre.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.regalo_libre.auth.config.OauthPropertiesConfig;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthManagementApiService {
    private final OauthPropertiesConfig oauthPropertiesConfig;

    public OAuthManagementApiToken getManagementApiToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", oauthPropertiesConfig.getClientId());
        map.add("client_secret", oauthPropertiesConfig.getClientSecret());
        map.add("audience", oauthPropertiesConfig.getApiManagementAudience());
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        return restTemplate.exchange(
                oauthPropertiesConfig.getApiManagementTokenUrl(),
                HttpMethod.POST,
                requestEntity,
                OAuthManagementApiToken.class
        ).getBody();
    }

    public MercadoLibreAccessToken getMercadoLibreUserTokenInfoFromApiManagement(String accessToken, String sub) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        MercadoLibreTokenInfo response = restTemplate.exchange(
                oauthPropertiesConfig.getApiManagementUsersUrl() + sub,
                HttpMethod.GET,
                entity,
                MercadoLibreTokenInfo.class
        ).getBody();
        assert response != null;
        return response.mercadoLibreAccessToken;
    }

    private static class MercadoLibreTokenInfo {
        @JsonProperty("mercadolibre_token_info")
        private MercadoLibreAccessToken mercadoLibreAccessToken;
    }

}



