package com.regalo_libre.auth;

import com.regalo_libre.auth.model.OAuthUser;
import org.springframework.context.ApplicationEvent;

public class OAuthUserCreatedEvent extends ApplicationEvent {
    private final OAuthUser oAuthUser;

    public OAuthUserCreatedEvent(Object source, OAuthUser user) {
        super(source);
        this.oAuthUser = user;
    }

    public OAuthUser getoAuthUser() {
        return oAuthUser;
    }
}
