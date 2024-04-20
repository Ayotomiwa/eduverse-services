package dev.captain.chatservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Document(collection = "channels")
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    private String id;
    private String topic;
    private String creator;
    private String creatorId;
    private String moduleId;
    private LocalDate createdDate;
    private LocalTime createdTime;
}
