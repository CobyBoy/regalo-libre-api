package com.regalo_libre.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl {
    private final ProfileRepository profileRepository;

    Profile find(Long userId) {
        return profileRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }


    PublicProfileDTO findPublicProfile(String username) {
        var profile = profileRepository.findByAppNickname(username).orElseThrow(() -> new ProfileNotPublicException("Este perfil es privado"));
        return PublicProfileDTO.toDto(profile);
    }

    EditProfileDTO editProfile(EditProfileDTO profile) {
        var profileFound = profileRepository.findById(profile.id()).orElseThrow();
        profileFound.setBiography(profile.biography());
        profileFound.setAppNickname(profile.appNickname());
        profileFound.setIsPrivate(profile.isPrivate());
        return EditProfileDTO.builder().build().toDto(profileRepository.save(profileFound));
    }
}
