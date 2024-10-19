package com.regalo_libre.follower;

import com.regalo_libre.auth.model.Auth0User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_following")
public class UserFollowing {
    @EmbeddedId
    private UserFollowingId id;

    @ManyToOne
    @MapsId("followerId")
    private Auth0User follower;

    @ManyToOne
    @MapsId("followeeId")
    private Auth0User followee;

    @Column(name = "followed_date")
    private LocalDateTime followedDate;

    @Column(name = "is_following")
    private boolean isFollowing;

    @Column(name = "is_followed_by")
    private boolean isFollowedBy;
}
