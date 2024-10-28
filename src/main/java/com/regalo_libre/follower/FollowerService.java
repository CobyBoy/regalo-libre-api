package com.regalo_libre.follower;

import com.regalo_libre.auth.Auth0UserService;
import com.regalo_libre.profile.Profile;
import com.regalo_libre.profile.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowerService {
    private final Auth0UserService auth0UserService;
    private final ProfileService profileService;
    private final UserFollowingRepository userFollowingRepository;

    @Transactional
    public UserFollowingDTO follow(Long followerId, Long followeeId) {
        var follower = auth0UserService.findAuth0UserById(followerId);
        var followee = auth0UserService.findByProfileId(followeeId);
        UserFollowingId id = new UserFollowingId(follower.getId(), followee.get().getId());
        UserFollowing userFollowing = userFollowingRepository.findById(id)
                .orElse(new UserFollowing());

        userFollowing.setId(id);
        userFollowing.setFollower(follower);
        userFollowing.setFollowee(followee.get());
        userFollowing.setFollowedDate(LocalDateTime.now());
        userFollowing.setFollowing(true);

        userFollowingRepository.save(userFollowing);
        boolean isFollowedBy = userFollowingRepository.findByFollowerIdAndFolloweeId(followerId, followee.get().getId()).get().isFollowedBy();
        userFollowing.setFollowedBy(isFollowedBy);
        userFollowingRepository.save(userFollowing);

        return UserFollowingDTO.builder()
                .following(userFollowing.isFollowing())
                .followed_by(userFollowing.isFollowedBy())
                .followed_date(userFollowing.getFollowedDate())
                .build();

    }

    @Transactional
    public List<FollowerDTO> getFollowers(String username) {
        Profile profile = profileService.findProfileByAppNickname(username);
        var user = auth0UserService.findByProfileId(profile.getProfileId());
        return userFollowingRepository.findFollowersByUserId(user.get().getId());
    }

    @Transactional
    public List<FollowerDTO> getFollowees(String username) {
        Profile profile = profileService.findProfileByAppNickname(username);
        var user = auth0UserService.findByProfileId(profile.getProfileId());
        return userFollowingRepository.findFolloweesByUserId(user.get().getId());
    }
}
