package com.regalo_libre.follower;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserFollowingDTO(
        LocalDateTime followed_date,
        boolean following,
        boolean followed_by
) {
}
