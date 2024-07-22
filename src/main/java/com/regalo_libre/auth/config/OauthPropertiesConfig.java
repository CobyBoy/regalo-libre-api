package com.regalo_libre.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "okta.oauth2")
public class OauthPropertiesConfig {
    private String authorizationUrl;
    private String issuer;
    private String clientId;
    private String clientSecret;
    private String audience;
    private String jwksUrl;
    private String redirectLoginUri;
    private String loginTokenUrl;
    private String userInfoUrl;
    private String apiManagementAudience;
    private String apiManagementTokenUrl;
    private String apiManagementUsersUrl;
}
