package com.regalo_libre.profile;

public interface ProfileService {
    Profile getProfileByUserId(Long userId);

    PublicProfileDTO getPublicProfileByUserNickname(String username);

    EditProfileDTO editProfile(EditProfileDTO profile);
}
