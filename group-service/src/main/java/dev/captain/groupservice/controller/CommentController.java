package dev.captain.groupservice.controller;


import dev.captain.groupservice.model.Comment;
import dev.captain.groupservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/group-service/discussions")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{discussionId}/comments")
    public ResponseEntity<?> getComments(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sort,
            @RequestParam Optional<Long> size,
            @PathVariable String discussionId) {

        PageRequest p = PageRequest.of(page.orElse(0), size.orElse(20L).intValue(), Sort.by(sort.orElse("createdAt"), sortBy.orElse("desc")));
        Page<Comment> comments = commentService.getComments(discussionId, p);

        if (comments.isEmpty()) {
            return ResponseEntity.badRequest().body("No comments found for this thread");
        }
        return ResponseEntity.ok(comments);
    }


    @PostMapping("/{discussionId}/comments")
    public ResponseEntity<?> addComment(@RequestBody Comment comment, @PathVariable String discussionId) {
        if (comment == null) {
            return ResponseEntity.badRequest().body("Comment cannot be empty");
        }
        if (discussionId == null || comment.getComment() == null) {
            return ResponseEntity.badRequest().body("Please provide a thread id & comment");
        }
        if (comment.getUserId() == null) {
            return ResponseEntity.badRequest().body("Please provide the creator's id");
        }
        Comment savedComment = commentService.create(comment, discussionId);
        return ResponseEntity.ok(savedComment);
    }


    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable("commentId") String commentId, @RequestParam Long userId, @RequestParam boolean isLiked) {
        commentService.likeComment(commentId, userId, isLiked);
        return ResponseEntity.ok("Comment liked");
    }

    @PostMapping("/comments/{commentId}/reply")
    public ResponseEntity<?> replyComment(@PathVariable("commentId") String commentId, @RequestBody Comment reply) {
        if (reply == null) {
            return ResponseEntity.badRequest().body("Reply cannot be empty");
        }
        if (reply.getComment() == null) {
            return ResponseEntity.badRequest().body("Please provide a reply");
        }
        if (reply.getUserId() == null) {
            return ResponseEntity.badRequest().body("Please provide the creator's id");
        }
        Comment savedReply = commentService.replyComment(commentId, reply);
        return ResponseEntity.ok(savedReply);
    }


    @GetMapping("/comments/{parentCommentId}/replies")
    public ResponseEntity<?> getReplies(@PathVariable String parentCommentId) {
        List<Comment> replies = commentService.getReplies(parentCommentId);
        if (replies.isEmpty()) {
            return ResponseEntity.badRequest().body("No replies found for this comment");
        }
        return ResponseEntity.ok(replies);
    }


}
