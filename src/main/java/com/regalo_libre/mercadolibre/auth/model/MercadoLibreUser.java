package com.regalo_libre.mercadolibre.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.profile.Profile;
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

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_bookmark",
            schema = "meli",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "meli_id", referencedColumnName = "id"))
    private List<BookmarkedProduct> bookmarkedProducts = new ArrayList<>();

    @OneToOne(mappedBy = "mercadoLibreUser", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private MercadoLibreAccessToken accessToken;

    @OneToOne(mappedBy = "mercadoLibreUser", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Profile profile;

    public void addBookmarkedProduct(BookmarkedProduct product) {
        this.bookmarkedProducts.add(product);
    }
}
