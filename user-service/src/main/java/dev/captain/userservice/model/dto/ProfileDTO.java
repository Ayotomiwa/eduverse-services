package dev.captain.userservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePicUrl;
    private String coverPicUrl;
    private String username;
    private Long followingCount;
    private Long followersCount;
    private Long userId;
    private Long facultyId;
    private String facultyName;
}