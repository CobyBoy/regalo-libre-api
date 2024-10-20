package com.regalo_libre.mercadolibre.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/meli")
public class MercadoLibreAuthController {
    private final MercadoLibreAccessTokenService mercadoLibreAccessTokenService;
    @Value("${ui.url}")
    private String uiUrl;

    @GetMapping("code")
    public RedirectView getMercadoLibreUserData(@RequestParam(name = "code") String code) {
        mercadoLibreAccessTokenService.exchangeCodeForToken(code);
        log.info("Redirecting after log in with Mercado Libre to {}", uiUrl + "/me/lists");
        return new RedirectView(uiUrl + "/me/lists");
    }
}
