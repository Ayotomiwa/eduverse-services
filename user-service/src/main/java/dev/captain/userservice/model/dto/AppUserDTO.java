package dev.captain.userservice.model.dto;


import dev.captain.userservice.model.enums.AUTHORITY;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AppUserDTO {
    private Long id;
    private String username;
    private String firstName;
    private String password;
    private String lastName;
    private String email;
    private String profilePicUrl;
    private String userType;
    private AUTHORITY authority;
    private StaffDTO staff;
    private StudentDTO student;
    private FacultyDTO faculty;
}
