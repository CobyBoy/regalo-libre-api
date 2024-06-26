package com.meli.wishlist.auth;

import com.meli.wishlist.domain.mercadolibre.auth.meli.model.MercadoLibreUser;
import com.meli.wishlist.keycloak.IKeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/user")
public class KeycloakController {
    private final IKeycloakService keycloakService;

    @CrossOrigin(origins = "https://localhost:4200")
    @PostMapping
    public ResponseEntity<MercadoLibreUser> createKeycloakUser(@RequestBody MercadoLibreUser mercadoLibreUser) {
        var found = keycloakService.searchUserByUsername(mercadoLibreUser.getNickname().replace(" ", "_"));
        if (found.isEmpty()) {
            log.info("Creating user" + mercadoLibreUser);
            keycloakService.createUser(mercadoLibreUser);
        }

        return ResponseEntity.ok(mercadoLibreUser);
    }
}
