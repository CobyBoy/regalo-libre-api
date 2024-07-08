package com.regalo_libre.auth;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KeycloakProvider {
    private final KeycloakConfig keycloakConfig;

    public RealmResource getRealmResource() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakConfig.getServerUrl())
                .realm(keycloakConfig.getRealmMaster())
                .clientId(keycloakConfig.getAdminCli())
                .username(keycloakConfig.getUserConsole())
                .password(keycloakConfig.getPasswordConsole())
                .clientSecret(keycloakConfig.getClientSecret())
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();
        return keycloak.realm(keycloakConfig.getRealmName());
    }

    public UsersResource getUserResource() {
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }
}
