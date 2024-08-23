package com.regalo_libre.login;

import com.regalo_libre.auth.config.Auth0PropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final Auth0PropertiesConfig auth0PropertiesConfig;

    @GetMapping("/api/v1/login")
    public RedirectView authorize() {
        String authorizationUrl = auth0PropertiesConfig.getAuthorizationUrl() +
                "?response_type=code" +
                "&audience=" + auth0PropertiesConfig.getAudience() +
                "&client_id=" + auth0PropertiesConfig.getClientId() +
                "&redirect_uri=" + auth0PropertiesConfig.getRedirectLoginUri() +
                "&scope=openid profile";

        return new RedirectView(authorizationUrl);
    }

    @GetMapping("/api/v1/logout")
    public RedirectView logout() {
        RestTemplate restTemplate = new RestTemplate();
        String logoutRedirect = "https://localhost:4200/logout";
        String url = UriComponentsBuilder.fromHttpUrl(auth0PropertiesConfig.getLogoutUrl())
                .queryParam("client_id", auth0PropertiesConfig.getClientId())
                .queryParam("returnTo", logoutRedirect)
                .toUriString();

        restTemplate.getForObject(url, String.class);
        return new RedirectView(logoutRedirect);
    }
}
