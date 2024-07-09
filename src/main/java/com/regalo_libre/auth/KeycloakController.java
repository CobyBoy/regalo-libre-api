package com.regalo_libre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/user")
public class KeycloakController {
    private final IKeycloakService keycloakService;
    private final KeycloakConfig keycloakConfig;

    @PostMapping
    public ResponseEntity<MercadoLibreUserDTO> createKeycloakUser(@RequestBody MercadoLibreUserDTO mercadoLibreUser) {
        List<UserRepresentation> found = keycloakService.searchUserByUsername(mercadoLibreUser.nickname().replace(" ", "_"));
        if (found.isEmpty()) {
            log.info("Creating user" + mercadoLibreUser);
            keycloakService.createUser(mercadoLibreUser);
        }
        return ResponseEntity.ok(mercadoLibreUser);
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginUserDTO userDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", keycloakConfig.getGrantType());
        body.add("username", userDto.nickname().replace(" ", "_"));
        body.add("password", String.valueOf(userDto.id()));
        return getResponseEntity(keycloakConfig.getTokenUrl(), headers, body);
    }

    @PostMapping("logout")
    public ResponseEntity logout(@RequestBody String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("refresh_token", refreshToken);
        return getResponseEntity(keycloakConfig.getLogoutUrl(), headers, body);
    }

    private ResponseEntity getResponseEntity(String url, HttpHeaders headers, MultiValueMap<String, String> body) {
        body.add("client_id", keycloakConfig.getClientId());
        body.add("client_secret", keycloakConfig.getClientSecret());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
