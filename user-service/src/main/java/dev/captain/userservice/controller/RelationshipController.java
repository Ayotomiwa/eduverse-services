package dev.captain.userservice.controller;


import dev.captain.userservice.service.RelationshipService;
import dev.captain.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user-service/relationship")
@RestController
@RequiredArgsConstructor
public class RelationshipController {

    private final UserService userService;
    private final RelationshipService relationshipService;

    @GetMapping("/followers")
    public ResponseEntity<?> followers(@RequestParam Long userId) {
        if (userId == null) return null;
        if (userService.existsUserById(userId)) return null;

        return ResponseEntity.ok(relationshipService.getFollowers(userId));
    }

    @GetMapping("/following")
    public ResponseEntity<? >  following(@RequestParam Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("UserId cannot be null");
        }
        if (!userService.existsUserById(userId)) {
           return ResponseEntity.badRequest().body("User does not exist");
        }

        return ResponseEntity.ok(relationshipService.getFollowing(userId));
    }

    @GetMapping("/follow")
    public ResponseEntity<?> follow(@RequestParam Long followerId,
                                    @RequestParam Long followId,
                                    @RequestParam boolean follow) {
        if (followerId == null || followId == null) {
            return ResponseEntity.badRequest().body("FollowerId, FollowId & follow cannot be null");
        }
        if(follow) {
            boolean following = relationshipService.follow(followerId, followId);
            if (following) {
                return ResponseEntity.ok("Followed successfully");
            }
            return ResponseEntity.badRequest().body("Failed to follow");
        }

        boolean unfollowing = relationshipService.unfollow(followerId, followId);
        if (unfollowing) {
            return ResponseEntity.ok("Unfollowed successfully");
        }
        return ResponseEntity.badRequest().body("Failed to unfollow");
    }


    @GetMapping("")
    public ResponseEntity<?> isFollowing(@RequestParam Long followerId, @RequestParam Long followingId) {
        if (followerId == null || followingId == null){
            return ResponseEntity.badRequest().body("FollowerId and FollowId cannot be null");
        }
        return ResponseEntity.ok(relationshipService.isFollowing(followerId, followingId));
    }



}
