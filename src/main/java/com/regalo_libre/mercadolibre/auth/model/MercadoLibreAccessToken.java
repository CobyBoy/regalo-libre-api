package com.regalo_libre.mercadolibre.auth.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_token", schema = "meli")
public class MercadoLibreAccessToken {
    @Id
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private int expiresIn;
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;
}
