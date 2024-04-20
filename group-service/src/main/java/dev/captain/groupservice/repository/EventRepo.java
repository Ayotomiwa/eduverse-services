package dev.captain.groupservice.repository;

import dev.captain.groupservice.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepo extends MongoRepository<Event, String> {


    List<Event> findByGroupId(String groupId);
}
