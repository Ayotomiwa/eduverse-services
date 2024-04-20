package dev.captain.userservice.service;


import dev.captain.userservice.model.tables.AppUser;
import dev.captain.userservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipService {

    private final UserRepo userRepo;


    public List<AppUser> getFollowers(Long userId) {
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return new ArrayList<>();
        if(user.getFollowers() == null) return new ArrayList<>();
        return user.getFollowers();
    }

    public List<AppUser> getFollowing(Long userId) {
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return new ArrayList<>();
        if(user.getFollowedUsers() == null) return new ArrayList<>();
        return user.getFollowedUsers();
    }

    public boolean follow(Long followerId, Long userToFollowId) {
        AppUser follower = userRepo.findById(followerId).orElse(null);
        AppUser userToFollow = userRepo.findById(userToFollowId).orElse(null);
        if (follower == null || userToFollow == null) {
            return false;
        }
        if (follower.getFollowedUsers() == null) {
            follower.setFollowedUsers(new ArrayList<>());
        }
        if (userToFollow.getFollowers() == null) {
            userToFollow.setFollowers(new ArrayList<>());
        }

        follower.getFollowedUsers().add(userToFollow);
        userToFollow.getFollowers().add(follower);
        userRepo.save(follower);
        userRepo.save(userToFollow);
        return true;
    }

    public boolean unfollow(Long followerId, Long userToUnfollowId) {
        AppUser follower = userRepo.findById(followerId).orElse(null);
        AppUser userToUnfollow = userRepo.findById(userToUnfollowId).orElse(null);
        if (follower == null || userToUnfollow == null) {
            return false;
        }
       if(follower.getFollowedUsers() == null || userToUnfollow.getFollowers() == null) {
           return false;
       }

       if(!follower.getFollowedUsers().contains(userToUnfollow)
               || !userToUnfollow.getFollowers().contains(follower)){
           return false;
       }

        follower.getFollowedUsers().remove(userToUnfollow);
        userToUnfollow.getFollowers().remove(follower);
        userRepo.save(follower);
        userRepo.save(userToUnfollow);
        return true;
    }


    public boolean isFollowing(Long followerId, Long followingId) {
        AppUser follower = userRepo.findById(followerId).orElse(null);
        AppUser toFollow = userRepo.findById(followingId).orElse(null);
        if (follower == null || toFollow == null) {
            return false;
        }
        if (follower.getFollowedUsers() == null || toFollow.getFollowers() == null) {
            return false;
        }
        return follower.getFollowedUsers().contains(toFollow) && toFollow.getFollowers().contains(follower);
    }
}
