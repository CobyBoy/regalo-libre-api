package com.meli.wishlist.mercadolibre.auth.meli;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MercadoLibreUserThumbnail {
    @JsonProperty("picture_id")
    private String pictureId;
    @JsonProperty("picture_url")
    private String pictureUrl;
}
