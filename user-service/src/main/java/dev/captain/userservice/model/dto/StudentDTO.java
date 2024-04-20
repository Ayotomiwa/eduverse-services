package dev.captain.userservice.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentDTO {
    private String studentNumber;
    private String school;
    private String course;
    private int startYear;
}
