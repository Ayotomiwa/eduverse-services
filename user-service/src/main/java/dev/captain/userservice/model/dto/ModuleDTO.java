package dev.captain.userservice.model.dto;


import dev.captain.userservice.model.tables.Course;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
public class ModuleDTO {
    private Long id;
    private String name;
    private String description;
    private String profileUrl;
    private String code;
    private String about;
    private Set<AppUserDTO> teachingTeam;
    private Set<AppUserDTO> students;
    private Course course;
}
