package com.regalo_libre.bookmarks.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OnMercadoLibreAccessTokenRetrievedPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(Long userId) {
        log.info("Publishing event from thread: " + Thread.currentThread().getName());
        OnMercadoLibreAccessTokenRetrievedEvent retrievedEvent = new OnMercadoLibreAccessTokenRetrievedEvent(this, userId);
        applicationEventPublisher.publishEvent(retrievedEvent);
        log.info("Event published");
    }
}
