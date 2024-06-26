package com.regalo_libre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {
    UserRepresentation createUser(MercadoLibreUser user);

    List<UserRepresentation> searchUserByUsername(String username);

    List<UserRepresentation> findAllUsers();
}