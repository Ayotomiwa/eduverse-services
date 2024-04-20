package dev.captain.groupservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Document
public class Discussion {
    @Id
    String id;
    String groupId;
    String topic;
    Long userId;
    String username;
    LocalDateTime createdAt;
    List<Long> likesIds;
    Long count;
}
