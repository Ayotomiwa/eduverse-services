package dev.captain.postservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private String id;
    private String postId;
    private String userId;
    private String username;
    private String userProfileUrl;
    private LocalDateTime createdAt;
    private String comment;
    private List<Long> likesIds;
    private List<Comment> replies;
}
