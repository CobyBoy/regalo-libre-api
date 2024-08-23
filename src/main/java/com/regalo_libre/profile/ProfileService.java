package com.regalo_libre.profile;

public interface ProfileService {
    Profile getProfile(Long userId);

    PublicProfileDTO findPublicProfileByUserNickname(String username);

    EditProfileDTO editProfile(EditProfileDTO profile);
}
