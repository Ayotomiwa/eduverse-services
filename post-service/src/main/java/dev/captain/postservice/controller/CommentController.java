package dev.captain.postservice.controller;

import dev.captain.postservice.model.Comment;
import dev.captain.postservice.service.CommentService;
import dev.captain.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/post-service")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getCommentsByPost(@PathVariable("postId") String postId) {
        if (!CheckIfPostExists(postId)) {
            return ResponseEntity.badRequest().body("Post does not exist");
        }

        List<Comment> comments = commentService.getComments(postId);
        if (comments.isEmpty()) {
            return ResponseEntity.badRequest().body("No comments found for this post");
        }

        return ResponseEntity.ok(commentService.getComments(postId));
    }


    @GetMapping("/comments")
    public ResponseEntity<?> getComments(@RequestBody List<String> commentIds) {
        List<Comment> comments = commentService.getCommentsByIdsIn(commentIds);
        if (comments.isEmpty()) {
            return ResponseEntity.badRequest().body("No comments found for this post");
        }

        return ResponseEntity.ok(comments);
    }


    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {

        ResponseEntity<String> isPostValid = isPostValid(comment.getPostId());

        if (isPostValid.getStatusCode().is4xxClientError()) {
            return isPostValid;
        }

        ResponseEntity<String> isCommentValid = isCommentValid(comment);
        if (isCommentValid.getStatusCode().is4xxClientError()) {
            return isCommentValid;
        }

        comment.setCreatedAt(LocalDateTime.now());
        commentService.addComment(comment);
        return ResponseEntity.ok("successfully added comment");
    }


    @PostMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") String commentId) {
        if (commentService.deleteComment(commentId)) {
            return ResponseEntity.ok("Comment deleted successfully");
        }
        return ResponseEntity.badRequest().body("Comment does not exist");
    }


    @PostMapping("comments/{commentId}/reply")
    public ResponseEntity<?> replyToComment(@PathVariable("commentId") String commentId, @RequestBody Comment reply) {

        ResponseEntity<String> isCommentValid = isCommentValid(reply);
        if (isCommentValid.getStatusCode().is4xxClientError()) {
            return isCommentValid;
        }

        if (!commentService.exists(commentId)) {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }

        if (commentService.replyToComment(commentId, reply)) {
            return ResponseEntity.ok("Reply added successfully");
        }
        return ResponseEntity.badRequest().body("Reply could not be added to comment. Please try again.");
    }


    @PostMapping("comments/{commentId}/like/{isLiked}")
    public ResponseEntity<?> likeComment(@PathVariable("commentId") String commentId,
                                         @PathVariable("isLiked") boolean isLiked,
                                         @RequestBody Long userId) {
        if (!commentService.exists(commentId)) {
            return ResponseEntity.badRequest().body("Comment does not exist");
        }
        commentService.likeComment(commentId, userId, isLiked);
        return ResponseEntity.ok("Comment liked successfully");
    }


    public ResponseEntity<String> isPostValid(String postId) {
        if (postId == null) {
            return ResponseEntity.badRequest().body("PostId is required");
        }
        if (!CheckIfPostExists(postId)) {
            return ResponseEntity.badRequest().body("Post does not exist");
        }
        return ResponseEntity.ok("Post is valid");
    }


    public ResponseEntity<String> isCommentValid(Comment comment) {
        if (comment.getComment() == null || comment.getComment().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment is required");
        }
        if (comment.getUserId() == null || comment.getUsername() == null) {
            return ResponseEntity.badRequest().body("UserId is required");
        }
        return ResponseEntity.ok("Comment is valid");
    }


    public boolean CheckIfPostExists(String postId) {
        return postService.postExists(postId);
    }

}
