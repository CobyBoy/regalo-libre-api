package com.regalo_libre.auth;

import com.regalo_libre.auth.response.OAuthUserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth-profile")
public class OAuthProfileController {
    private final OAuthUserServiceImpl oAuthUserService;

    @GetMapping
    public ResponseEntity<OAuthUserInfoDTO> getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        OAuthUserInfo response = oAuthUserService.getOauthUserInfo(authorizationHeader);

        return ResponseEntity.ok(
                OAuthUserInfoDTO.builder()
                        .build()
                        .toDto(oAuthUserService.createOauthUser(response))
        );
    }
}
