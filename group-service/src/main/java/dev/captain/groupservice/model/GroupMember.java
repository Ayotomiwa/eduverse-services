package dev.captain.groupservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMember {
    Long userId;
    String username;
    String profilePicUrl;
}
