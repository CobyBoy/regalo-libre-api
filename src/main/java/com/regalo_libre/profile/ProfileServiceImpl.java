package com.regalo_libre.profile;

import com.regalo_libre.auth.OAuthUserService;
import com.regalo_libre.auth.model.OAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final OAuthUserService oAuthUserService;

    public Profile getProfile(Long userId) {
        OAuthUser user = oAuthUserService.getOAuthUserById(userId);
        return profileRepository.findById(user.getProfile().getProfileId()).orElseThrow(() -> new ProfileNotPublicException("Usuario no encontrado"));
    }

    public PublicProfileDTO findPublicProfileByUserNickname(String username) {
        Profile profile = profileRepository.findByAppNickname(username).orElseThrow(() -> new ProfileNotPublicException("Este perfil es privado"));
        return PublicProfileDTO.toDto(profile);
    }

    public EditProfileDTO editProfile(EditProfileDTO profile) {
        Profile profileFound = profileRepository.findById(profile.id()).orElseThrow();
        profileFound.setBiography(profile.biography());
        profileFound.setAppNickname(profile.appNickname());
        profileFound.setIsPrivate(profile.isPrivate());
        return EditProfileDTO.builder().build().toDto(profileRepository.save(profileFound));
    }
}
