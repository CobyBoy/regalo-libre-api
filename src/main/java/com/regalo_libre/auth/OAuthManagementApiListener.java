package com.regalo_libre.auth;

import com.regalo_libre.auth.model.OAuthUser;
import com.regalo_libre.mercadolibre.auth.MercadoLibreAccessTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuthManagementApiListener implements ApplicationListener<OAuthUserCreatedEvent> {
    private final OAuthManagementApiService managementApiService;
    private final OAuthManagementApiRepository oAuthManagementApiRepository;
    private final MercadoLibreAccessTokenService mercadoLibreAccessTokenService;

    @Override
    public void onApplicationEvent(OAuthUserCreatedEvent event) {
        log.info("Handling event {}", event.getoAuthUser());
        OAuthUser user = event.getoAuthUser();
        OAuthManagementApiToken token = managementApiService.getManagementApiToken();
        token.setUserId(user.getId());
        oAuthManagementApiRepository.save(token);
        var mercadoLibreAccessToken = managementApiService.getMercadoLibreUserTokenInfoFromApiManagement(token.getAccessToken(), user.getSub());
        mercadoLibreAccessToken.setUserId(user.getId());
        mercadoLibreAccessTokenService.saveAccessToken(mercadoLibreAccessToken);
    }

}
