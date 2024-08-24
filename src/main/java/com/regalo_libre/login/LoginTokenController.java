package com.regalo_libre.login;

import com.regalo_libre.auth.jwt.Auth0AccessToken;
import com.regalo_libre.login.exception.Auth0TokenIsUndefinedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginTokenController {
    private final LoginTokenServiceImpl loginTokenService;

    @GetMapping("/api/v1/token")
    public ResponseEntity<Object> getAuth0Token(@RequestParam("code") String code) {
        if (code.equals("undefined")) {
            throw new Auth0TokenIsUndefinedException("Error obteniendo código de autorización.");
        }
        Auth0AccessToken response = loginTokenService.getAuth0Token(code);
        return ResponseEntity.ok(response);
    }

}