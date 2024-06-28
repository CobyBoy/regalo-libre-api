package com.regalo_libre.mercadolibre.bookmark;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import com.regalo_libre.wishlist.model.WishList;
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
@Table(name = "bookmark", schema = "meli")
public class BookmarkedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unique_id")
    private Long uniqueId;
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    @Column(name = "currency_id")
    @JsonProperty("currency_id")
    private String currencyId;
    private String permalink;

    @ManyToMany(mappedBy = "gifts")
    @JsonIgnore
    private List<WishList> wishLists = new ArrayList<>();

    @ManyToMany(mappedBy = "bookmarkedProducts")
    @JsonIgnore
    private List<MercadoLibreUser> users;

    private Long price;
    private String status;
    @Transient
    private List<String> subStatus;
    private String thumbnail;
    private String title;
    private String bookmarkedDate;

    public void setUsers(List<MercadoLibreUser> users) {
        this.users = users;
        for (MercadoLibreUser user : users) {
            user.addBookmarkedProduct(this);
        }
    }
}
