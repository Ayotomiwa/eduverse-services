package dev.captain.chatservice.model;


import dev.captain.chatservice.controller.enums.MSSG_TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chats")
public class Chat {
    @Id
    private String id;
    private String message;
    private String sender;
    private Long senderId;
    private String channelId;
    private String moduleId;
    private MSSG_TYPE type;
    private LocalDateTime timestamp;

}
