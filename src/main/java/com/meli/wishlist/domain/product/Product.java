package com.meli.wishlist.domain.product;

import com.meli.wishlist.domain.product.picture.Picture;
import com.meli.wishlist.domain.wishlist.model.WishList;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product extends AbstractPersistableEntity<String>{
    @Id
    private String id;
    private String permalink;

    @ManyToMany(mappedBy = "products")
    private Set<WishList> wishLists;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval=true,fetch = FetchType.EAGER)
    private Set<Picture> pictures = new HashSet<>();

    private Long price;
    private String status;
    @Transient
    private Set<String> subStatus;
    private String thumbnail;
    private String title;

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
        for (Picture picture : pictures) {
            picture.setProduct(this);
        }
    }
}
