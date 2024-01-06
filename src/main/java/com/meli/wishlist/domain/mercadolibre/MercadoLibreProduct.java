package com.meli.wishlist.domain.mercadolibre;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meli.wishlist.domain.wishlist.model.WishList;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MercadoLibreProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unique_id")
    private Integer uniqueId;
    @Column(name = "id")
    private String id;
    private String permalink;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval=true,fetch = FetchType.EAGER)
    private List<MercadoLibrePicture> pictures = new ArrayList<>();

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<WishList> wishLists = new ArrayList<>();

    private Long price;
    private String status;
    @Transient
    private List<String> subStatus;
    private String thumbnail;
    private String title;

    public void setPictures(List<MercadoLibrePicture> pictures) {
        this.pictures = pictures;
        for (MercadoLibrePicture picture : pictures) {
            picture.setProduct(this);
        }
    }
}
