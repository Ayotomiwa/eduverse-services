package dev.captain.postservice.model;

import dev.captain.postservice.model.Enums.POST_ACCESS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String caption;
    private String imageUrl;
    private Long userId;
    private Long universityId;
    private String username;
    private Long facultyId;
    private String facultyName;
    private String userProfileUrl;
    private POST_ACCESS access;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private Poll poll;
    private List<Long> likesIds;
    private List<String> commentIds;

}
