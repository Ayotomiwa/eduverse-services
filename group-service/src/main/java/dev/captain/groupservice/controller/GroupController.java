package dev.captain.groupservice.controller;


import dev.captain.groupservice.model.Group;
import dev.captain.groupservice.model.GroupMember;
import dev.captain.groupservice.model.UserGroup;
import dev.captain.groupservice.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/group-service/")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final RestTemplate restTemplate;


    @GetMapping("university/{universityId}/groups")
    public ResponseEntity<?> getGroupsByUniversity(@PathVariable("universityId") Long universityId) {
        List<Group> groups = groupService.getGroupsByUniversity(universityId);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("university/{universityId}/groups")
    public ResponseEntity<?> createGroup(@RequestBody Group newGroup, @PathVariable("universityId") Long universityId) {
        if (newGroup == null) {
            return ResponseEntity.badRequest().body("Group cannot be empty");
        }
        if (newGroup.getName() == null || newGroup.getType() == null || newGroup.getCategory() == null) {
            return ResponseEntity.badRequest().body("Please provide a name, type & category");
        }
        if (newGroup.getCreatorId() == null) {
            return ResponseEntity.badRequest().body("Please provide a creator id");
        }

        String fileName = newGroup.getProfilePicUrl();
        if (fileName != null) {
            String awsFileName = "https://eduverse-v1.s3.eu-west-2.amazonaws.com/" + universityId + "/" + fileName;
            newGroup.setProfilePicUrl(awsFileName);
        }

        Group savedGroup = groupService.createGroup(newGroup, universityId);
        return ResponseEntity.ok(savedGroup);
    }

    @GetMapping("groups/{groupId}/members")
    public ResponseEntity<?> getMembersByGroup(@PathVariable("groupId") String groupId, @RequestParam boolean isAccepted) {
        List<UserGroup> members = groupService.getMembersByGroup(groupId, isAccepted);
        return ResponseEntity.ok(members);
    }


    @PostMapping("groups/{groupId}/members")
    public ResponseEntity<?> addMemberToGroup(@PathVariable("groupId") String groupId, @RequestBody GroupMember newMember) {
        System.out.println("Adding member to group" + newMember.getUserId() + " " + newMember.getUsername());
        UserGroup userGroup = groupService.addMemberToGroup(groupId, newMember);
        if (userGroup == null) {
            return ResponseEntity.badRequest().body("User not added to group");
        }
        return ResponseEntity.ok(userGroup);
    }

    @PostMapping("groups/{groupId}/members/{userId}/leave")
    public ResponseEntity<?> leaveGroup(@PathVariable("groupId") String groupId, @PathVariable("userId") Long userId) {
        boolean left = groupService.leaveGroup(groupId, userId);
        if (!left) {
            return ResponseEntity.badRequest().body("User not removed from group");
        }
        return ResponseEntity.ok("User removed from group");
    }


    @PostMapping("groups/{groupId}/members/multiple")
    public ResponseEntity<?> addMultipleMembersToGroup(@PathVariable("groupId") String groupId, @RequestBody List<GroupMember> newMembers) {
        List<GroupMember> membersAdded = new ArrayList<>();
        for (GroupMember member : newMembers) {
            try {
                UserGroup userGroup = groupService.addMemberToGroup(groupId, member);
                if (userGroup == null) {
                    return ResponseEntity.badRequest().body("User already a member of this group");
                }
                membersAdded.add(member);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error adding member to group");
            }
        }
        if (membersAdded.size() != newMembers.size()) {
            return ResponseEntity.badRequest().body("Some members were not added to group. Please check the members list");
        }
        return ResponseEntity.ok("All members added to group");
    }


    @GetMapping("users/{userId}/groups")
    public ResponseEntity<?> getGroupsByUser(@PathVariable("userId") Long userId) {
        List<Group> groups = groupService.getGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }


    @GetMapping("groups/{groupId}")
    public ResponseEntity<?> getGroupById(@PathVariable("groupId") String groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
//            return ResponseEntity.badRequest().body("Group not found");
            return null;
        }
        return ResponseEntity.ok(group);
    }


    @GetMapping("groups/search")
    public ResponseEntity<?> searchGroups(
            @RequestParam String query,
            @RequestParam(required = false) Long universityId,
            @RequestParam(required = false) Long userId) {

        if (userId == null && universityId == null) {
            return ResponseEntity.badRequest().body("Please provide a university id or user id");
        }
        List<Group> groups = groupService.searchGroups(query, universityId, userId);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("groups/delete")
    public ResponseEntity<?> deleteModule(@RequestBody List<String> groupIds, @RequestParam("user-id") Long userId) {
        if (groupIds == null || groupIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Module name cannot be null or empty");
        }
        if (userId == null) {
            return ResponseEntity.badRequest().body("User id cannot be null");
        }


//        String authority = restTemplate.exchange("https://user-service-dgrsoybfsa-ew.a.run.app/api/user-service/authority?user-id=" + userId,
                String authority = restTemplate.exchange("https://user-service-dgrsoybfsa-ew.a.run.app/api/user-service/users/" + userId +  "/authority",
                HttpMethod.GET, null, String.class, new ParameterizedTypeReference<String>(){
                }).getBody();


        if (!Objects.equals(authority, "ADMIN")) {
            return ResponseEntity.badRequest().body("User does not authorized to perform this operation");
        }

        groupService.deleteGroup(groupIds);
        return ResponseEntity.ok("Groups deleted successfully");
    }




}
