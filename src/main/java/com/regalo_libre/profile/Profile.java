package com.regalo_libre.profile;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private String name;
    @Column(name = "meli_nickname", updatable = false)
    private String meliNickname;
    @Column(unique = true, nullable = false, name = "app_nickname")
    private String appNickname;
    Boolean isPrivate;
    @Column(length = 500)
    String biography;
    String pictureUrl;
}
