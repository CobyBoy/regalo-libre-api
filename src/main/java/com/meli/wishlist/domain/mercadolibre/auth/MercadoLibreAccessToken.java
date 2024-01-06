package com.meli.wishlist.domain.mercadolibre.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private int expiresIn;
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;

}
