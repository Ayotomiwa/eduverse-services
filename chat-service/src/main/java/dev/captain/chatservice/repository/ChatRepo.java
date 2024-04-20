package dev.captain.chatservice.repository;


import dev.captain.chatservice.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends MongoRepository<Chat, String> {

    List<Chat> findByChannelId(String channelId);
}
