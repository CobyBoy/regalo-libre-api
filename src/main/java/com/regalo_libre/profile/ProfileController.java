package com.regalo_libre.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileServiceImpl profileService;

    @GetMapping
    public ResponseEntity<Profile> getProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(profileService.find(userId));
    }

    @PostMapping
    public ResponseEntity<EditProfileDTO> editProfile(@RequestBody EditProfileDTO profileDTO) {
        return ResponseEntity.ok(profileService.editProfile(profileDTO));
    }

    @GetMapping("/{username}")
    public ResponseEntity<PublicProfileDTO> getPublicProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.findPublicProfile(username));
    }
}
