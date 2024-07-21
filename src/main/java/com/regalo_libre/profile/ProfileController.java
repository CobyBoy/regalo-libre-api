package com.regalo_libre.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<Profile> getProfile(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<EditProfileDTO> editProfile(@RequestBody EditProfileDTO profileDTO) {
        return ResponseEntity.ok(profileService.editProfile(profileDTO));
    }

    @GetMapping("public/{username}")
    public ResponseEntity<PublicProfileDTO> getPublicProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getPublicProfileByUserNickname(username));
    }
}
