package dev.captain.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Document
public class Notification {
    Long id;
    String discussionId;
    String postId;
    String commentId;
    String eventId;
    String chatId;
    String message;
    LocalDate date;
    LocalTime time;

}
