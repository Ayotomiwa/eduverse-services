package dev.captain.groupservice.service;

import dev.captain.groupservice.model.Comment;
import dev.captain.groupservice.model.Discussion;
import dev.captain.groupservice.repository.CommentRepo;
import dev.captain.groupservice.repository.DiscussionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final DiscussionRepo discussionRepo;


    public Page<Comment> getComments(String discussionId, PageRequest p) {
        return commentRepo.findAllByDiscussionId(discussionId, p);

    }

    public Comment create(Comment comment, String discussionId) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setDiscussionId(discussionId);
        Comment saved = commentRepo.save(comment);
        Discussion discussion = discussionRepo.findById(discussionId).orElse(null);
        if (discussion != null) {
            if (discussion.getCount() == null) {
                discussion.setCount(1L);
            } else {
                discussion.setCount(discussion.getCount() + 1);
            }
            discussionRepo.save(discussion);
        }

        return commentRepo.save(saved);
    }

    public void likeComment(String commentId, Long userId, boolean isLiked) {
        Comment comment = commentRepo.findById(commentId).orElse(null);
        if (comment != null) {
            List<Long> likesIds = comment.getLikesIds();
            if (likesIds == null) {
                if (!isLiked) {
                    return;
                }
                likesIds = List.of(userId);
                comment.setLikesIds(likesIds);
            } else {
                if (likesIds.contains(userId)) {
                    if (!isLiked) {
                        likesIds.remove(userId);
                        comment.setLikesIds(likesIds);
                        commentRepo.save(comment);
                    }
                } else {
                    if (isLiked) {
                        likesIds.add(userId);
                        comment.setLikesIds(likesIds);
                        commentRepo.save(comment);
                    }
                }
            }
        }
    }

    public Comment replyComment(String commentId, Comment reply) {
        Comment parentComment = commentRepo.findById(commentId).orElse(null);
        if (parentComment == null) {
            return null;
        }
        reply.setDiscussionId(parentComment.getDiscussionId());
        reply.setCreatedAt(LocalDateTime.now());
        reply.setParentComment(parentComment);
        return commentRepo.save(reply);
    }

    public List<Comment> getReplies(String parentCommentId) {
        return commentRepo.findAllByParentCommentId(parentCommentId);
    }
}
