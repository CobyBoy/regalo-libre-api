package com.regalo_libre.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileServiceImpl profileService;

    @GetMapping
    public ResponseEntity getProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(profileService.find(userId));
    }

    @GetMapping("/public")
    public ResponseEntity getPublicProfile() {
        return ResponseEntity.ok("Public");
    }
}
