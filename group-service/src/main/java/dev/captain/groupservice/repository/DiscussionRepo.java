package dev.captain.groupservice.repository;

import dev.captain.groupservice.model.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DiscussionRepo extends MongoRepository<Discussion, String> {
    Page<Discussion> findAllByGroupId(String groupId, PageRequest p);
}
