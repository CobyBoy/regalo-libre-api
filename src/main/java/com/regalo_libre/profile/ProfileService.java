package com.regalo_libre.profile;

public interface ProfileService {
    Profile getProfile(Long userId);

    PublicProfileDTO findPublicProfileByUserNickname(String username, Long followerId);

    /*PublicProfileDTO findPublicProfileByProfileId(Long profileId, Long followerId);*/

    EditProfileDTO editProfile(EditProfileDTO profile);

    Profile findProfileByAppNicknameAndIsPrivateFalse(String username);
}
