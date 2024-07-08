package com.regalo_libre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
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

    @CrossOrigin(origins = "https://localhost:4200")
    @PostMapping
    public ResponseEntity<MercadoLibreUser> createKeycloakUser(@RequestBody MercadoLibreUser mercadoLibreUser) {
        List<UserRepresentation> found = keycloakService.searchUserByUsername(mercadoLibreUser.getNickname().replace(" ", "_"));
        if (found.isEmpty()) {
            log.info("Creating user" + mercadoLibreUser);
            keycloakService.createUser(mercadoLibreUser);
        }
        return ResponseEntity.ok(mercadoLibreUser);
    }

    @CrossOrigin(origins = "https://localhost:4200")
    @PostMapping("login")
    public ResponseEntity login(@RequestBody UserDTO userDto) {
        String url = "http://localhost:9090/realms/wishlist-realm-dev/protocol/openid-connect/token";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", keycloakConfig.getClientId());
        body.add("grant_type", keycloakConfig.getGrantType());
        body.add("username", userDto.nickname().replace(" ", "_"));
        body.add("password", String.valueOf(userDto.id()));
        body.add("client_secret", keycloakConfig.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
