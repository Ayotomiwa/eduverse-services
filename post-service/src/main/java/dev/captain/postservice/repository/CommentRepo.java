package dev.captain.postservice.repository;

import dev.captain.postservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepo extends MongoRepository<Comment, String> {
    List<Comment> findAllByPostId(String postId);
}
