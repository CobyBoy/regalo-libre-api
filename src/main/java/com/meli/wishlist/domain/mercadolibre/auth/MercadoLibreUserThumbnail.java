package com.meli.wishlist.domain.mercadolibre.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MercadoLibreUserThumbnail {
    @Id
    @JsonProperty("picture_id")
    private String pictureId;
    @JsonProperty("picture_url")
    private String pictureUrl;
    @OneToOne(mappedBy = "thumbnail")
    MercadoLibreUser mercadoLibreUser;
}
