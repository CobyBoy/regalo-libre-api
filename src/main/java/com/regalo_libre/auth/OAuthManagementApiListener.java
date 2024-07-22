package com.regalo_libre.auth;

import com.regalo_libre.auth.model.OAuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuthManagementApiListener implements ApplicationListener<OAuthUserCreatedEvent> {
    private final OAuthManagementApiService managementApiService;

    @Override
    public void onApplicationEvent(OAuthUserCreatedEvent event) {
        log.info("Handling event {}", event.getoAuthUser());
        OAuthUser user = event.getoAuthUser();
        OAuthManagementApiToken token = managementApiService.getManagementApiToken(user.getId());
        managementApiService.getMercadoLibreUserTokenInfoFromApiManagement(token.getAccessToken(), user.getSub(), user.getId());
    }

}
