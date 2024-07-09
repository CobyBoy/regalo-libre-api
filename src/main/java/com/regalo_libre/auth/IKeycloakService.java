package com.regalo_libre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {
    UserRepresentation createUser(MercadoLibreUserDTO user);

    List<UserRepresentation> searchUserByUsername(String username);

    List<UserRepresentation> findAllUsers();
}