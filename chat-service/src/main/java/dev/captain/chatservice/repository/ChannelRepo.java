package dev.captain.chatservice.repository;

import dev.captain.chatservice.model.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepo extends MongoRepository<Channel, String> {


    List<Channel> findByModuleId(String moduleId);
}
