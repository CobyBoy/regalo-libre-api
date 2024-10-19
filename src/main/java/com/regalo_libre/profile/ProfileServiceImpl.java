package com.regalo_libre.profile;

import com.regalo_libre.auth.Auth0UserService;
import com.regalo_libre.auth.model.Auth0User;
import com.regalo_libre.follower.UserFollowingRepository;
import com.regalo_libre.profile.exception.ProfileNicknameAlreadyExists;
import com.regalo_libre.profile.exception.ProfileNotPublicException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final Auth0UserService auth0UserService;
    private final UserFollowingRepository userFollowingRepository;

    public Profile getProfile(Long userId) {
        Auth0User user = auth0UserService.getAuth0UserById(userId);
        return profileRepository.findById(user.getProfile().getProfileId()).orElseThrow(() -> new ProfileNotPublicException("Usuario no encontrado"));
    }

    @Transactional
    public PublicProfileDTO findPublicProfileByUserNickname(String username, Long followerId) {
        Profile profile = profileRepository.findByAppNicknameAndIsPrivateFalse(username).orElseThrow(() -> new ProfileNotPublicException("Este perfil es privado"));
        var user = auth0UserService.findByProfileId(profile.getProfileId());
        var follower = userFollowingRepository.findByFollowerIdAndFolloweeId(followerId, user.get().getId());
        var followers = userFollowingRepository.findFollowersByUserId(user.get().getId());
        var followees = userFollowingRepository.findFolloweesByUserId(user.get().getId());
        boolean isFollowing = false;
        boolean isFollowedBy = false;
        if (follower.isPresent()) {
            isFollowing = follower.get().isFollowing();
            isFollowedBy = follower.get().isFollowedBy();
        }
        return PublicProfileDTO.toDto(profile, followers.size(), followees.size(), user.get(), isFollowing, isFollowedBy);
    }

    @Transactional
    public EditProfileDTO editProfile(EditProfileDTO profile) {
        Optional<Profile> profileByNickname = profileRepository.findByAppNickname(profile.appNickname());
        Profile profileFound = profileRepository.findById(profile.id()).orElseThrow();
        if (profileByNickname.isPresent() && !profileFound.getAppNickname().equals(profile.appNickname())) {
            throw new ProfileNicknameAlreadyExists("Este nombre de usuario no está disponible");
        }
        profileFound.setBiography(profile.biography());
        profileFound.setAppNickname(profile.appNickname());
        profileFound.setIsPrivate(profile.isPrivate());
        return EditProfileDTO.builder().build().toDto(profileRepository.save(profileFound));
    }

    /*public PublicProfileDTO findPublicProfileByProfileId(Long profileId, Long followerId) {
        Profile profile = profileRepository.findByProfileIdAndIsPrivateFalse(profileId).orElseThrow(() -> new ProfileNotPublicException("Este perfil es privado"));
        var user = auth0UserService.findByProfileId(profile.getProfileId());
        var follower = userFollowingRepository.findByFollowerIdAndFolloweeId(followerId, user.get().getId());
        boolean isFollowing = false;
        boolean isFollowedBy = false;
        if(follower.isPresent()) {
            isFollowing = follower.get().isFollowing();
            isFollowedBy = follower.get().isFollowedBy();
        }
        return PublicProfileDTO.toDto(profile, followers.size(), user.get(), isFollowing, isFollowedBy);
    }*/

    public Profile findProfileByAppNicknameAndIsPrivateFalse(String username) {
        return profileRepository.findByAppNicknameAndIsPrivateFalse(username).orElseThrow(() -> new ProfileNotPublicException("Este perfil es privado"));
    }
}
