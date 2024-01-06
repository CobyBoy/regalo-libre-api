package com.meli.wishlist.domain.mercadolibre.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MercadoLibreUser {
    @Id
    private Integer id;
    private String nickname;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    @OneToOne(cascade = CascadeType.ALL)
    private MercadoLibreUserThumbnail thumbnail;
}
