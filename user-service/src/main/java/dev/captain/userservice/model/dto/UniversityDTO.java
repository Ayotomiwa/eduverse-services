package dev.captain.userservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String universityName;
    private String domain;
    private String userPassword;
}
