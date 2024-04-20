package dev.captain.eventservice.repo;


import dev.captain.eventservice.model.GroupEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupEventRepo extends MongoRepository<GroupEvent, String> {
    List<GroupEvent> findAllByGroupId(String groupId);

    List<GroupEvent> findAllByGroupIdIn(List<String> groupIds);
}
