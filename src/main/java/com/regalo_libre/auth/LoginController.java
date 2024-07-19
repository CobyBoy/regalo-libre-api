package com.regalo_libre.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final OauthPropertiesConfig oauthPropertiesConfig;

    @GetMapping("/api/v1/login")
    public RedirectView authorize() {
        String authorizationUrl = oauthPropertiesConfig.getAuthorizationUrl() +
                "?response_type=code" +
                "&audience=" + oauthPropertiesConfig.getAudience() +
                "&client_id=" + oauthPropertiesConfig.getClientId() +
                "&redirect_uri=" + oauthPropertiesConfig.getRedirectLoginUri() +
                "&scope=openid profile";

        return new RedirectView(authorizationUrl);
    }
}
