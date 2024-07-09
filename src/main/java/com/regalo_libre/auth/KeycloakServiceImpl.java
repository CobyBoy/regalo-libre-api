package com.regalo_libre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUserDTO;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakServiceImpl implements IKeycloakService {
    private final KeycloakProvider keycloakProvider;

    @Override
    public UserRepresentation createUser(MercadoLibreUserDTO user) {
        int status;
        UsersResource usersResource = keycloakProvider.getUserResource();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(user.firstName());
        userRepresentation.setLastName(user.lastName());
        userRepresentation.setEmail(user.email());
        userRepresentation.setUsername(user.nickname().replace(" ", "_"));
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response = usersResource.create(userRepresentation);
        log.info(response.getHeaders().toString());
        status = response.getStatus();
        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(user.id().toString());
            usersResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = keycloakProvider.getRealmResource();
            List<RoleRepresentation> representations = List.of(realmResource.roles().get("user-realm").toRepresentation());
            realmResource
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(representations);
            return realmResource.users().get(userId).toRepresentation();
        } else {
            log.error(response.getStatusInfo().toString());
            log.error("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
            log.error("Response body: " + response.readEntity(String.class));
            return null;
        }
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return keycloakProvider.getRealmResource().users().searchByUsername(username, true);
    }

    @Override
    public List<UserRepresentation> findAllUsers() {
        return keycloakProvider.getRealmResource().users().list();
    }
}