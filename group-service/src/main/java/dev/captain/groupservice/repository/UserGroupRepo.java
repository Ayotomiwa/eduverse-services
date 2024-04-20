package dev.captain.groupservice.repository;

import dev.captain.groupservice.model.Group;
import dev.captain.groupservice.model.UserGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserGroupRepo extends MongoRepository<UserGroup, String> {

    Optional<List<UserGroup>> findAllByUserId(Long userId);

    Optional<List<UserGroup>> findAllByGroupIdAndAccepted(String groupId, boolean isAccepted);

    boolean existsByGroupIdAndUserId(String groupId, Long userId);


    void deleteByGroupIdAndUserId(String groupId, Long userId);

    Optional<UserGroup> findByGroupIdAndUserId(String groupId, Long userId);
}
