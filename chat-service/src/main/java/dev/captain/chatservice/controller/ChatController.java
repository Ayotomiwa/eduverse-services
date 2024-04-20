package dev.captain.chatservice.controller;


import dev.captain.chatservice.model.Chat;
import dev.captain.chatservice.repository.ChatRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatRepo chatRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{channelId}")
    public Chat sendMessage(@Payload Chat chat, @DestinationVariable String channelId) {
        chat.setTimestamp(LocalDateTime.now());
        System.out.println("Chat: " + chat);
        chatRepo.save(chat);
        messagingTemplate.convertAndSend("/topic/channel" + channelId, chat);
        return chat;
    }


    @MessageMapping("/join")
    @SendTo("/topic/public")
    public Chat addUser(@Payload Chat chat, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chat.getSender());
        return chat;
    }


}
