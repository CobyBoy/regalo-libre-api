package com.meli.wishlist.domain.mercadolibre.auth.meli.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meli.wishlist.domain.mercadolibre.model.BookmarkedProduct;
import com.meli.wishlist.domain.mercadolibre.auth.meli.MercadoLibreUserThumbnail;
import com.meli.wishlist.domain.wishlist.model.WishList;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "muser", schema = "meli")
public class MercadoLibreUser {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;
    private String nickname;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    @Embedded
    private MercadoLibreUserThumbnail thumbnail;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WishList> wishLists = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_bookmark",
            schema = "meli",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "meli_id", referencedColumnName = "id"))

    private List<BookmarkedProduct> bookmarkedProducts = new ArrayList<>();

    public void addWishList(WishList wishList) {
        wishLists.add(wishList);
        wishList.setUser(this);
    }

    public void addBookmarkedProduct(BookmarkedProduct product) {
        this.bookmarkedProducts.add(product);
    }
}
