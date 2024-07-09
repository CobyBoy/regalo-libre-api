package com.regalo_libre.wishlist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regalo_libre.mercadolibre.bookmark.BookmarkedProduct;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import com.regalo_libre.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wishlist", schema = "wishlist")
public class WishList {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false, name = "private_id")
    private UUID privateId;
    @Column(unique = true, nullable = false, updatable = false, name = "public_id")
    private String publicId;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column
    private Boolean isPrivate;

    @ManyToMany
    @JoinTable(
            name = "wishlist_bookmark",
            schema = "wishlist",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "bookmark_id", referencedColumnName = "id"))
    private List<BookmarkedProduct> gifts = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private MercadoLibreUser user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Transient
    private int totalGifts;

    public void setUser(MercadoLibreUser user) {
        this.user = user;
    }

    @PrePersist
    public void generateIds() {
        if (this.privateId == null) {
            this.privateId = UUID.randomUUID();
        }
        if (this.publicId == null || this.publicId.isEmpty()) {
            this.publicId = IdGenerator.generatePublicId();
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    public void calculateTotalProducts() {
        this.totalGifts = this.gifts != null ? this.gifts.size() : 0;
    }
}
