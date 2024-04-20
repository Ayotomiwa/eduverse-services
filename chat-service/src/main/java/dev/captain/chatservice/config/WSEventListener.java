package dev.captain.chatservice.config;


import dev.captain.chatservice.controller.enums.MSSG_TYPE;
import dev.captain.chatservice.model.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@RequiredArgsConstructor
public class WSEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String channelId = (String) headerAccessor.getSessionAttributes().get("channelId");
        if (username != null) {
            Chat chat = new Chat();
            chat.setType(MSSG_TYPE.LEAVE);
            chat.setSender(username);
            messagingTemplate.convertAndSend("/topic/channel" + channelId, chat);
        }
    }

}

