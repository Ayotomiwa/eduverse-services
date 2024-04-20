package dev.captain.userservice.service.Mapper;

import dev.captain.userservice.model.dto.AppUserDTO;
import dev.captain.userservice.model.dto.ModuleDTO;
import dev.captain.userservice.model.tables.Module;
import dev.captain.userservice.model.tables.Staff;
import dev.captain.userservice.model.tables.Student;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ModuleMapper {

    public static ModuleDTO convertModuleToDto(Module module) {
        if (module == null) {
            return new ModuleDTO();
        }
        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setId(module.getId());
        moduleDTO.setName(module.getName());
        moduleDTO.setDescription(module.getDescription());
        moduleDTO.setCode(module.getCode());
        moduleDTO.setAbout(module.getAbout());
        if (module.getTeachingTeam() != null) {
            Set<AppUserDTO> teachingTeam = new HashSet<>();
            for (Staff staff : module.getTeachingTeam()) {
                teachingTeam.add(UserMapper.convertUserToDto(staff.getUser()));
            }
            moduleDTO.setTeachingTeam(teachingTeam);
        }
        if (module.getStudents() != null) {
            Set<AppUserDTO> students = new HashSet<>();
            for (Student student : module.getStudents()) {
                students.add(UserMapper.convertUserToDto(student.getUser()));
            }
            moduleDTO.setStudents(students);
        }
        moduleDTO.setCourse(module.getCourse());
        return moduleDTO;
    }
}
