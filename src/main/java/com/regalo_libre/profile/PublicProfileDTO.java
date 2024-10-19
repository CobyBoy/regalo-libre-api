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
        boolean followed_by,
        boolean show_follow_button
) {
    static PublicProfileDTO toDto(Profile profile, int followers, int followees, Boolean isFollowing, Boolean isFollowedBy, Boolean showFollowButton) {
        return PublicProfileDTO.builder()
                .biography(profile.getBiography())
                .picture_url(profile.getPictureUrl())
                .nickname(profile.getAppNickname())
                .profile_id(profile.getProfileId())
                .follower_count(followers)
                .following_count(followees)
                .following(isFollowing)
                .followed_by(isFollowedBy)
                .show_follow_button(showFollowButton)
                .build();
    }
}
