package com.regalo_libre.auth;

import com.regalo_libre.auth.response.Auth0UserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth0-profile")
public class Auth0ProfileController {
    private final Auth0UserService auth0UserService;

    @GetMapping
    public ResponseEntity<Auth0UserInfoDTO> getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        Auth0UserInfo response = auth0UserService.getAuth0UserInfo(authorizationHeader);

        return ResponseEntity.ok(
                Auth0UserInfoDTO.builder()
                        .build()
                        .toDto(auth0UserService.createAuth0User(response))
        );
    }
}
