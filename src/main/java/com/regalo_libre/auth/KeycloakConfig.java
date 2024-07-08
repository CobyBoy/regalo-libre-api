package com.regalo_libre.auth;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KeycloakConfig {
    @Value("${keycloak.admin_cli}")
    private String adminCli;
    @Value("${keycloak.realm_name}")
    private String realmName;
    @Value("${keycloak.client_secret}")
    private String clientSecret;
    @Value("${keycloak.server_url}")
    private String serverUrl;
    @Value("${keycloak.realm_master}")
    private String realmMaster;
    @Value("${keycloak.user_console}")
    private String userConsole;
    @Value("${keycloak.password_console}")
    private String passwordConsole;
    @Value("${keycloak.client_id}")
    private String clientId;
    @Value("${keycloak.grant_type}")
    private String grantType;
}
