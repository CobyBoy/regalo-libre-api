package com.regalo_libre.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regalo_libre.mercadolibre.auth.model.MercadoLibreUser;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile", schema = "wishlist")
public class Profile {
    @Id
    @Column(name = "user_id")
    private Long userId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private MercadoLibreUser mercadoLibreUser;
    private String firstName;
    private String lastName;
    @Column(name = "meli_nickname")
    private String meliNickname;
    @Column(unique = true, nullable = false, name = "app_nickname")
    private String appNickname;
    Boolean isPrivate;
    @Column(length = 500)
    String biography;
    String pictureUrl;
}
