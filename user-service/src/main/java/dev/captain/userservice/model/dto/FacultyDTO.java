package dev.captain.userservice.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacultyDTO {
    private Long id;
    private String name;
    private String department;
}
