package com.regalo_libre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
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
