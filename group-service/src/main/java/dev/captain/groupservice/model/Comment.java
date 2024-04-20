package dev.captain.groupservice.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@RequiredArgsConstructor
@Data
public class Comment {
    @Id
    String id;
    String discussionId;
    Long userId;
    String username;
    String comment;
    Comment parentComment;
    LocalDateTime createdAt;
    List<Long> likesIds;

}
