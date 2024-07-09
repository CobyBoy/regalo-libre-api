package com.regalo_libre.profile;

import lombok.Builder;

@Builder
public record PublicProfileDTO(
        String biography,
        String picture_url,
        String nickname
) {
    static PublicProfileDTO toDto(Profile profile) {
        return PublicProfileDTO.builder()
                .biography(profile.getBiography())
                .picture_url(profile.getPictureUrl())
                .nickname(profile.getAppNickname())
                .build();
    }
}
