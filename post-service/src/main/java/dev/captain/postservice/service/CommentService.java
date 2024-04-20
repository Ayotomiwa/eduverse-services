package dev.captain.postservice.service;


import dev.captain.postservice.model.Comment;
import dev.captain.postservice.model.Post;
import dev.captain.postservice.repository.CommentRepo;
import dev.captain.postservice.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepo commentRepo;
    private final PostService postService;
    private final PostRepo postRepo;


    public List<Comment> getComments(String postId) {
        return commentRepo.findAllByPostId(postId);
    }

    public void addComment(Comment comment) {
        Comment savedComment = commentRepo.save(comment);
        Post commentedPost = postService.getPost(comment.getPostId());

        if (commentedPost.getCommentIds() == null) {
            commentedPost.setCommentIds(new ArrayList<>());
        }
        commentedPost.getCommentIds().add(savedComment.getId());
        postRepo.save(commentedPost);
    }

    public boolean deleteComment(String commentId) {
        if (commentRepo.existsById(commentId)) {
            commentRepo.deleteById(commentId);
            return true;
        }
        return false;
    }

    public boolean exists(String commentId) {
        return commentRepo.existsById(commentId);
    }

    public boolean replyToComment(String commentId, Comment reply) {
        if (commentRepo.existsById(commentId)) {
            Comment comment = commentRepo.findById(commentId).orElse(null);
            if (comment != null) {
                List<Comment> replies = comment.getReplies();
                if (replies == null) {
                    replies = new ArrayList<>();
                    replies.add(reply);
                    comment.setReplies(replies);
                } else {
                    replies.add(reply);
                    comment.setReplies(replies);
                }
                commentRepo.save(comment);
                return true;
            }
        }
        return false;
    }

    public List<Comment> getCommentsByIdsIn(List<String> commentIds) {
        return commentRepo.findAllById(commentIds);
    }


    public void likeComment(String commentId, Long userId, boolean like) {
        Comment comment = commentRepo.findById(commentId).orElse(null);
        if (comment != null) {
            List<Long> likesIds = comment.getLikesIds();
            if (likesIds == null) {
                if (!like) {
                    return;
                }
                likesIds = new ArrayList<>();
                likesIds.add(userId);
                comment.setLikesIds(likesIds);
            } else {
                if (likesIds.contains(userId)) {
                    if (!like) {
                        likesIds.remove(userId);
                        comment.setLikesIds(likesIds);
                    }
                    return;
                }
                likesIds.add(userId);
                comment.setLikesIds(likesIds);
            }
            commentRepo.save(comment);
        }
    }
}
