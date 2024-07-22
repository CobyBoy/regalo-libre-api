package com.regalo_libre.login;

import com.regalo_libre.auth.jwt.OAuthAccessToken;
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
    public ResponseEntity<Object> getOauthToken(@RequestParam("code") String code) {
        OAuthAccessToken response = loginTokenService.getOAuthToken(code);

        log.info("Returning oauth token {}", response);
        return ResponseEntity.ok(response);
    }

}