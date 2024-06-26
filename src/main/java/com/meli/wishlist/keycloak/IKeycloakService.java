package com.meli.wishlist.keycloak;

import com.meli.wishlist.domain.mercadolibre.auth.meli.model.MercadoLibreUser;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakService {
    UserRepresentation createUser(MercadoLibreUser user);

    List<UserRepresentation> searchUserByUsername(String username);

    List<UserRepresentation> findAllUsers();
}
