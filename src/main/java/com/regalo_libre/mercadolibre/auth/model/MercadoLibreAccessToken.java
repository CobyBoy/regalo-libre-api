package com.regalo_libre.mercadolibre.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_token", schema = "meli")
public class MercadoLibreAccessToken {
    @Id
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    private String scope;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
