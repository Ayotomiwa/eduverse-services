package dev.captain.chatservice.service;


import dev.captain.chatservice.model.Channel;
import dev.captain.chatservice.model.Chat;
import dev.captain.chatservice.repository.ChannelRepo;
import dev.captain.chatservice.repository.ChatRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepo channelRepo;
    private final ChatRepo chatRepo;

    public List<Channel> getChannels(String moduleId) {
        return channelRepo.findByModuleId(moduleId);
    }


    public Channel createChannel(String moduleId, Channel channel) {
        channel.setModuleId(moduleId);
        channel.setCreatedDate(LocalDate.now());
        channel.setCreatedTime(LocalTime.now());
        return channelRepo.save(channel);
    }

    public Channel getChannel(String channelId) {
        return channelRepo.findById(channelId).orElse(null);
    }

    public List<Chat> getChannelMessages(String channelId) {
        return chatRepo.findByChannelId(channelId);
    }
}
