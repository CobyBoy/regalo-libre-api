package com.regalo_libre.login;

import com.regalo_libre.auth.jwt.Auth0AccessToken;
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
        Auth0AccessToken response = loginTokenService.getAuth0Token(code);

        log.info("Returning auth0 token {}", response);
        return ResponseEntity.ok(response);
    }

}