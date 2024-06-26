package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/token")
public class MercadoLibreAuthController {
    private final IMercadoLibreAuthClientServiceImpl mercadoLibreAuthClientServiceImpl;

    @CrossOrigin(origins = "https://localhost:4200")
    @GetMapping
    public ResponseEntity<MercadoLibreUser> getMercadoLibreUserData(@RequestParam(name = "authorizationCode") String authorizationCode) {
        return ResponseEntity.ok(mercadoLibreAuthClientServiceImpl.getMercadoLibreUserData(authorizationCode));
    }
}
