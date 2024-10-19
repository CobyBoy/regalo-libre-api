package com.regalo_libre.profile;

import jakarta.validation.Valid;
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
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PostMapping
    public ResponseEntity<EditProfileDTO> editProfile(@Valid @RequestBody EditProfileDTO profileDTO) {
        return ResponseEntity.ok(profileService.editProfile(profileDTO));
    }

    @GetMapping("public")
    public ResponseEntity<PublicProfileDTO> findPublicProfileByUserNickname(@RequestParam String username, @AuthenticationPrincipal Long viewerId) {
        return ResponseEntity.ok(profileService.findPublicProfileByUserNickname(username, viewerId));
    }

    /*@GetMapping("public/{profileId}")
    public ResponseEntity<PublicProfileDTO> findPublicProfileByProfileId(@PathVariable Long profileId, @AuthenticationPrincipal Long viewerId) {
        return ResponseEntity.ok(profileService.findPublicProfileByProfileId(profileId, viewerId));
    }*/
}
