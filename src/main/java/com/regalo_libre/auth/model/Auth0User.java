package com.regalo_libre.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.regalo_libre.follower.UserFollowing;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.profile.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth0user")
public class Auth0User {
    @Id
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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_bookmark",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "auth0_user_id"),
            inverseJoinColumns = @JoinColumn(name = "meli_id", referencedColumnName = "meli_id"))
    private List<BookmarkedProduct> bookmarkedProducts = new ArrayList<>();

    // Followers: Users who follow this user
    @OneToMany(mappedBy = "follower")
    private Set<UserFollowing> followers = new HashSet<>();

    //    // Following: Users this user is following
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "followers",
//            joinColumns = @JoinColumn(name = "follower_id"),  // The current user is following (follower)
//            inverseJoinColumns = @JoinColumn(name = "followee_id")  // The user being followed (followee)
//    )
    @OneToMany(mappedBy = "followee")
    private Set<UserFollowing> following = new HashSet<>();

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

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
        this.id = Long.parseLong(sub.substring(sub.lastIndexOf('|') + 1));
    }
}
