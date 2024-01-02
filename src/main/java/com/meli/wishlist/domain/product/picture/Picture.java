package com.meli.wishlist.domain.product.picture;
import com.meli.wishlist.domain.product.AbstractPersistableEntity;
import com.meli.wishlist.domain.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Picture extends AbstractPersistableEntity<String> {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private String maxSize;
    private String quality;
    private String secureUrl;
    private String size;
    private String url;
}
