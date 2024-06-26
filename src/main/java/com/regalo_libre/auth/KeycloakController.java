package com.regalo_libre.auth;

import com.regalo_libre.mercadolibre.auth.MercadoLibreUser;
import com.regalo_libre.utils.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/user")
public class KeycloakController {
    private final IKeycloakService keycloakService;

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
}
