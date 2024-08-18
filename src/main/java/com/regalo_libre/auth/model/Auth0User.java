package com.regalo_libre.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.profile.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth0user", schema = "auth0")
public class Auth0User {
    @Id
    @JsonProperty("sub")
    @Column(name = "auth0_user_id")
    private Long id;
    @JsonProperty("sub")
    private String sub;
    private String name;
    private String nickname;
    private String pictureUrl;
    @JsonProperty("updated_at")
    private String updatedAt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_bookmark",
            schema = "meli",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "auth0_user_id"),
            inverseJoinColumns = @JoinColumn(name = "meli_id", referencedColumnName = "meli_id"))
    private List<BookmarkedProduct> bookmarkedProducts = new ArrayList<>();

    public static class Auth0UserBuilder {
        public Auth0UserBuilder fullStringId(String fullString) {
            try {
                String[] parts = fullString.split("\\|");
                if (parts.length > 0) {
                    this.id = Long.parseLong(parts[parts.length - 1]);
                } else {
                    throw new IllegalArgumentException("String does not contain expected format");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Last part is not a valid number", e);
            }
            return this;
        }
    }

    public void addBookmarkedProduct(BookmarkedProduct product) {
        this.bookmarkedProducts.add(product);
    }
}
