package com.regalo_libre.follower;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/follow")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;

    @PostMapping("/{followeeId}")
    public ResponseEntity followUser(@PathVariable Long followeeId, @AuthenticationPrincipal Long followerId) {
        return ResponseEntity.ok(followerService.follow(followerId, followeeId));
    }

    @GetMapping("/followers")
    public ResponseEntity getFollowers(@RequestParam String username) {
        return ResponseEntity.ok(followerService.getFollowers(username));
    }

    @GetMapping("/followees")
    public ResponseEntity getFollowees(@RequestParam String username) {
        return ResponseEntity.ok(followerService.getFollowees(username));
    }
}
