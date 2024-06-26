package com.meli.mercadolibre.auth;

import com.meli.mercadolibre.auth.model.MercadoLibreUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/token")
public class MercadoLibreAuthController {
    private final MercadoLibreAuthClient mercadoLibreAuthClient;

    @CrossOrigin(origins = "https://localhost:4200")
    @GetMapping
    public ResponseEntity<MercadoLibreUser> getMercadoLibreUserData(@RequestParam(name = "authorizationCode") String authorizationCode) {
        return ResponseEntity.ok(mercadoLibreAuthClient.getMercadoLibreUserData(authorizationCode));
    }
}
