package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUserDTO;
import com.regalo_libre.mercadolibre.auth.service.MercadoLibreAuthClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tokenold")
public class MercadoLibreAuthController {
    private final MercadoLibreAuthClientServiceImpl mercadoLibreAuthClientServiceImpl;

    @GetMapping
    public ResponseEntity<MercadoLibreUserDTO> getMercadoLibreUserData(@RequestParam(name = "authorizationCode") String authorizationCode) {
        return ResponseEntity.ok(mercadoLibreAuthClientServiceImpl.getMercadoLibreUserData(authorizationCode));
    }
}
