package dev.captain.groupservice.service;


import dev.captain.groupservice.model.Group;
import dev.captain.groupservice.model.GroupMember;
import dev.captain.groupservice.model.UserGroup;
import dev.captain.groupservice.model.enums.MEMBERSHIP;
import dev.captain.groupservice.repository.GroupRepo;
import dev.captain.groupservice.repository.UserGroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepo groupRepo;
    private final UserGroupRepo userGroupRepo;
    private final RestTemplate restTemplate;


    public List<Group> getGroupsByUniversity(Long universityId) {
        return groupRepo.findAllByUniversityId(universityId);
    }

    public Group getGroupById(String groupId) {
        return groupRepo.findById(groupId).orElse(null);
    }


    public List<Group> getGroupsByUser(Long userId) {
        List<UserGroup> userGroups = userGroupRepo.findAllByUserId(userId).orElse(null);
        if (userGroups == null) {
            return null;
        }
        List<String> groupIds = userGroups.stream().map(UserGroup::getGroupId).toList();
        return groupRepo.findAllByIdIn(groupIds);
    }

    public Group createGroup(Group newGroup, Long universityId) {

        if (groupExists(newGroup.getId())) {
            return groupRepo.save(newGroup);
        }

        newGroup.setCreatedAt(LocalDateTime.now());
        newGroup.setUniversityId(universityId);
        if (newGroup.getApproved() == null) {
            newGroup.setApproved(false);
        }
        newGroup.setBlocked(false);
        newGroup.setApproved(isAdmin(newGroup.getCreatorId()));


        Group savedGroup = groupRepo.save(newGroup);
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(savedGroup.getCreatorId());
        userGroup.setUsername(savedGroup.getCreatorUsername());
        userGroup.setGroupId(savedGroup.getId());


        userGroup.setJoinedDate(LocalDateTime.now());
        userGroup.setRole(MEMBERSHIP.MODERATOR);
        userGroup.setAccepted(true);
        userGroupRepo.save(userGroup);

        return savedGroup;
    }

    private boolean isAdmin(Long userId) {

        String role = "";

        try {
            role = restTemplate.exchange("https://user-service-dgrsoybfsa-ew.a.run.app/api/user-service/users/" + userId + "/authority", HttpMethod.GET, null, String.class).getBody();
        } catch (Exception ignored) {

        }
        return Objects.equals(role, "ADMIN");
    }


    public List<UserGroup> getMembersByGroup(String groupId, boolean isAccepted) {
        return userGroupRepo.findAllByGroupIdAndAccepted(groupId, isAccepted).orElse(null);
    }

    public boolean groupExists(String groupId) {
        if (groupId == null) {
            return false;
        }
        return groupRepo.existsById(groupId);
    }

    public UserGroup addMemberToGroup(String groupId, GroupMember newMember) {
        UserGroup existingUserGroup = userGroupRepo.findByGroupIdAndUserId(groupId, newMember.getUserId()).orElse(null);
        if (existingUserGroup != null) {
            return null;
        }
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(newMember.getUserId());
        userGroup.setUsername(newMember.getUsername());
        userGroup.setGroupId(groupId);
        userGroup.setAccepted(true);
        userGroup.setJoinedDate(LocalDateTime.now());
        userGroup.setRole(MEMBERSHIP.MEMBER);

        return userGroupRepo.save(userGroup);
    }

    public List<Group> searchGroups(String query, Long universityId, Long userId) {

        query = "(?i).*" + query + ".*";

        if (userId == null) {
            return groupRepo.findByNameRegexAndUniversityId(query, universityId);
        }

          List<UserGroup> group = userGroupRepo.findAllByUserId(userId).orElse(null);
          if (group == null || group.isEmpty()) {
              return new ArrayList<>();
          }
          List<String> groupIds = group.stream().map(UserGroup::getGroupId).toList();
            return groupRepo.findByNameRegexAndIdIn(query, groupIds);

    }

    public boolean leaveGroup(String groupId, Long userId) {
        UserGroup userGroup = userGroupRepo.findByGroupIdAndUserId(groupId, userId).orElse(null);
        if (userGroup == null) {
            return false;
        }
        userGroupRepo.deleteByGroupIdAndUserId(groupId, userId);
        return true;
    }

    public void deleteGroup(List<String> moduleIds) {
        try {
            for (String moduleId : moduleIds) {
                groupRepo.deleteById(moduleId);
            }
        } catch (Exception ignored) {

        }
    }
}
