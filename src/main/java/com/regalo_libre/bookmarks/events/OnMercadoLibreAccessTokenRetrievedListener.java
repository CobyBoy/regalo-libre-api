package com.regalo_libre.bookmarks.events;

import com.regalo_libre.auth.Auth0UserService;
import com.regalo_libre.bookmarks.BookmarksSynchronizationService;
import com.regalo_libre.mercadolibre.auth.MercadoLibreAccessTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnMercadoLibreAccessTokenRetrievedListener implements ApplicationListener<OnMercadoLibreAccessTokenRetrievedEvent> {
    private final MercadoLibreAccessTokenService mercadoLibreAccessTokenService;
    private final Auth0UserService auth0UserService;
    private final BookmarksSynchronizationService bookmarksSyncService;

    @Override
    @Async("taskExecutor")
    public void onApplicationEvent(OnMercadoLibreAccessTokenRetrievedEvent event) {
        log.info("Event received in thread: " + Thread.currentThread().getName());
        log.info("Handling event with user id: " + event.getUserId());
        var token = mercadoLibreAccessTokenService.getMercadoLibreAccessToken(event.getUserId());
        var user = auth0UserService.getAuth0UserById(token.getUserId());
        bookmarksSyncService.saveAllInitialBookmarks(user.getId(), null);
        log.info("Event listener handled complete");
    }
}