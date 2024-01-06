package com.meli.wishlist.domain.mercadolibre.auth;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MercadoLibreAccessToken {
    @Id
    private Integer user_id;
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;

}
