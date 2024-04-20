package dev.captain.groupservice.repository;

import dev.captain.groupservice.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {

    Page<Comment> findAllByDiscussionId(String discussionId, PageRequest p);

    List<Comment> findAllByParentCommentId(String parentCommentId);
}
