package com.regalo_libre.profile;

import com.regalo_libre.auth.model.Auth0User;
import lombok.Builder;

@Builder
public record PublicProfileDTO(
        String biography,
        String picture_url,
        String nickname,
        Long profile_id,
        int follower_count,
        int following_count,
        boolean following,
        boolean followed_by
) {
    static PublicProfileDTO toDto(Profile profile, int followers, int followees, Auth0User user, Boolean isFollowing, Boolean isFollowedBy) {
        return PublicProfileDTO.builder()
                .biography(profile.getBiography())
                .picture_url(profile.getPictureUrl())
                .nickname(profile.getAppNickname())
                .profile_id(profile.getProfileId())
                .follower_count(followers)
                .following_count(followees)
                .following(isFollowing)
                .followed_by(isFollowedBy)
                .build();
    }
}
