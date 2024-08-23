package com.regalo_libre.mercadolibre.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/meli")
public class MercadoLibreAuthController {
    private final IMercadoLibreAccessTokenService mercadoLibreAccessTokenService;

    @GetMapping("code")
    public RedirectView getMercadoLibreUserData(@RequestParam(name = "code") String code) {
        mercadoLibreAccessTokenService.exchangeCodeForToken(code);
        return new RedirectView("https://192.168.0.37:4200/user/lists");
    }
}
