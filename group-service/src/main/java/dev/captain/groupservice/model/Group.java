package dev.captain.groupservice.model;


import dev.captain.groupservice.model.enums.GROUP_CATEGORY;
import dev.captain.groupservice.model.enums.GROUP_TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Group {
    @Id
    String id;
    String name;
    String description;
    String about;
    String profilePicUrl;
    Long creatorId;
    String creatorUsername;
    Long universityId;
    LocalDateTime createdAt;
    GROUP_TYPE type;
    GROUP_CATEGORY category;
    LocalDateTime blockedDate;
    LocalDateTime approvedDate;
    Boolean blocked;
    Boolean approved;
}
