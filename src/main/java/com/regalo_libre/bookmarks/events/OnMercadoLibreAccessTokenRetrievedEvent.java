package com.regalo_libre.bookmarks.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
public class OnMercadoLibreAccessTokenRetrievedEvent extends ApplicationEvent {
    private final Long userId;

    public OnMercadoLibreAccessTokenRetrievedEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
        log.info("Handling event in thread {}", Thread.currentThread().getName());
        log.info("Mercado libre token event {}", userId);
    }

    public Long getUserId() {
        return userId;
    }
}
