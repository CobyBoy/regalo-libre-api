package com.regalo_libre.login;

import com.regalo_libre.auth.config.OauthPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

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

    @GetMapping("/api/v1/logout")
    public RedirectView logout() {
        RestTemplate restTemplate = new RestTemplate();
        String logoutRedirect = "https://localhost:4200/logout";
        String url = UriComponentsBuilder.fromHttpUrl(oauthPropertiesConfig.getLogoutUrl())
                .queryParam("client_id", oauthPropertiesConfig.getClientId())
                .queryParam("returnTo", logoutRedirect)
                .toUriString();

        restTemplate.getForObject(url, String.class);
        return new RedirectView(logoutRedirect);
    }
}
