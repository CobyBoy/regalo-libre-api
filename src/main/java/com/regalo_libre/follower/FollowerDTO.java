package com.regalo_libre.follower;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FollowerDTO(
        Long id,
        String username,
        LocalDateTime followedDate
) {

}
