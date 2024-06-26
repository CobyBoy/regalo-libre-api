package com.meli.wishlist.domain.mercadolibre.auth.meli;

import com.meli.wishlist.domain.mercadolibre.auth.meli.model.MercadoLibreUser;
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
