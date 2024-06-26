package com.meli.keycloak;

import com.meli.mercadolibre.auth.model.MercadoLibreUser;
import com.meli.utils.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KeycloakServiceImpl implements IKeycloakService {
    @Override
    public UserRepresentation createUser(MercadoLibreUser user) {
        int status;
        UsersResource usersResource = KeycloakProvider.getUserResource();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setUsername(user.getNickname().replace(" ", "_"));
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
            credentialRepresentation.setValue(user.getId().toString());
            usersResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloakProvider.getRealmResource();
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
        return KeycloakProvider.getRealmResource().users().searchByUsername(username, true);
    }

    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloakProvider.getRealmResource().users().list();
    }
}
