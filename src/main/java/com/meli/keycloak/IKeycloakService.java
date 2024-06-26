package com.meli.keycloak;

import com.meli.mercadolibre.auth.model.MercadoLibreUser;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {
    UserRepresentation createUser(MercadoLibreUser user);

    List<UserRepresentation> searchUserByUsername(String username);

    List<UserRepresentation> findAllUsers();
}
