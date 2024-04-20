package dev.captain.groupservice.model;


import dev.captain.groupservice.model.enums.MEMBERSHIP;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserGroup {
    @Id
    String id;
    Long userId;
    String username;
    String groupId;
    MEMBERSHIP role;
    LocalDateTime joinedDate;
    Long totalLikes;
    Long totalComments;
    Long totalThreads;
    boolean accepted;
}
