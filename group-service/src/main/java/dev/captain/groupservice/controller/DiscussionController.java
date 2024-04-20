package dev.captain.groupservice.controller;


import dev.captain.groupservice.model.Discussion;
import dev.captain.groupservice.service.DiscussionService;
import dev.captain.groupservice.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/group-service")
public class DiscussionController {

    private final DiscussionService discussionService;
    private final GroupService groupService;


    @GetMapping("groups/{groupId}/discussions")
    public ResponseEntity<?> getDiscussions(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sort,
            @RequestParam Optional<Long> size,
            @PathVariable("groupId") String groupId) {

        if (!groupService.groupExists(groupId)) {
            return ResponseEntity.badRequest().body("Group does not exist");
        }
        PageRequest p = PageRequest.of(page.orElse(0), size.orElse(20L).intValue(), Sort.Direction.DESC, sortBy.orElse("createdAt"));
        Page<Discussion> discussions = discussionService.getDiscussions(groupId, p);

        if (discussions.isEmpty()) {
            return ResponseEntity.badRequest().body("No Discussion found for this group");
        }

        return ResponseEntity.ok(discussions);
    }


    @PostMapping("groups/{groupId}/discussions")
    public ResponseEntity<?> addDiscussion(@RequestBody Discussion discussion, @PathVariable String groupId) {
        if (discussion == null) {
            return ResponseEntity.badRequest().body("Discussion cannot be empty");
        }
        if (groupId == null || discussion.getTopic() == null) {
            return ResponseEntity.badRequest().body("Please provide a group id & topic");
        }
        if (discussion.getUserId() == null) {
            return ResponseEntity.badRequest().body("Please provide the creator id");
        }
        discussion.setCreatedAt(LocalDateTime.now());
        discussion.setGroupId(groupId);
        Discussion savedDiscussion = discussionService.create(discussion);
        return ResponseEntity.ok(savedDiscussion);
    }


    @GetMapping("/groups/discussions/{discussionId}")
    public ResponseEntity<?> getDiscussion(@PathVariable String discussionId) {
        Discussion discussion = discussionService.getDiscussion(discussionId);
        if (discussion == null) {
            return ResponseEntity.badRequest().body("Discussion not found");
        }
        return ResponseEntity.ok(discussion);
    }


    @PostMapping("groups/discussions/{discussionId}")
    public ResponseEntity<?> likeDiscussion(@PathVariable("discussionId") String discussionId, @RequestParam Long userId, @RequestParam boolean isLiked) {
        discussionService.likeDiscussion(discussionId, userId, isLiked);
        return ResponseEntity.ok("Discussion liked");
    }


}











