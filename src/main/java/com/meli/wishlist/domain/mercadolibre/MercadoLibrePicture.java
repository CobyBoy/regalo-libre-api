package com.meli.wishlist.domain.mercadolibre;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MercadoLibrePicture {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "meli_product_id", nullable = false)
    @JsonIgnore
    private MercadoLibreProduct product;
    @JsonProperty("max_size")
    private String maxSize;
    private String quality;
    @JsonProperty("secure_url")
    private String secureUrl;
    private String size;
    private String url;
}
