package com.meli.wishlist.domain.mercadolibre.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/token")
public class MercadoLibreAuthController {
    private final MercadoLibreAuthClient mercadoLibreAuthClient;

    @CrossOrigin(origins = "https://localhost:4200")
    @GetMapping
    public void getToken(@RequestParam(name = "authorizationCode") String authorizationCode) {
        mercadoLibreAuthClient.getToken(authorizationCode);
    }
}
