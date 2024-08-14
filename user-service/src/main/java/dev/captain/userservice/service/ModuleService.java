package dev.captain.userservice.service;

import dev.captain.userservice.model.dto.ModuleDTO;
import dev.captain.userservice.model.tables.Module;
import dev.captain.userservice.model.tables.*;
import dev.captain.userservice.repo.*;
import dev.captain.userservice.service.Mapper.ModuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepo moduleRepo;
    private final UniversityRepo universityRepo;
    private final CourseRepo courseRepo;
    private final StudentRepo studentRepo;
    private final StaffRepo staffRepo;
    private final UserRepo userRepo;

    public List<Module> getModules(Long universityId) {
        return moduleRepo.findByUniversityId(universityId);
    }


    public Module createModule(Module module, Long universityId) {
        University university = universityRepo.findById(universityId).get();
        module.setUniversity(university);
        if (module.getCourse() != null && module.getCourse().getId() != null) {
            Course course = courseRepo.findById(module.getCourse().getId()).orElse(null);
            module.setCourse(course);
        }
        return moduleRepo.save(module);
    }

    public boolean existsModule(Long moduleId) {
        return moduleRepo.existsById(moduleId);
    }


    public ModuleDTO getModule(Long moduleId) {
        return ModuleMapper.convertModuleToDto(moduleRepo.findById(moduleId).orElse(null));
    }


    public String addStudentToModule(Long moduleId, Long studentId) {
        Student student = studentRepo.findById(studentId).orElse(null);
        Module module = moduleRepo.findById(moduleId).orElse(null);
        if (student != null && module != null) {
            if (student.getModules() == null) {
                student.setModules(new HashSet<>());
            }
            student.getModules().add(module);
            studentRepo.save(student);
            return "Student added to module";
        }
        return "Student or module does not exist";
    }

    public String addStaffToModule(Long moduleId, Long staffId) {
        Staff staff = staffRepo.findById(staffId).orElse(null);
        Module module = moduleRepo.findById(moduleId).orElse(null);
        if (staff != null && module != null) {
            if (staff.getModules() == null) {
                staff.setModules(new HashSet<>());
            }
            staff.getModules().add(module);
            staffRepo.save(staff);
            return "Staff added to module";
        }
        return "Staff or module does not exist";
    }

    public Set<Module> getModuleByUser(Long userId) {
        AppUser user = userRepo.findById(userId).get();
        Set<Module> modules = new HashSet<>();
        if (user.getStudent() != null) {
            modules.addAll(studentRepo.findById(user.getStudent().getId()).get().getModules());
        }
        if (user.getStaff() != null) {
            modules.addAll(staffRepo.findById(user.getStaff().getId()).get().getModules());
        }
        return modules;
    }

    public String addStudentsToModule(Long moduleId, List<Long> studentIds) {

        for (Long studentId : studentIds) {
            addStudentToModule(moduleId, studentId);
        }
        return "Students added to module";
    }

    public String addStaffToModule(Long moduleId, List<Long> staffIds) {

        for (Long staffId : staffIds) {
            addStudentToModule(moduleId, staffId);
        }
        return "Students added to module";
    }

    public List<Module> searchModule(Long universityId, String query) {
        query = "%" + query + "%";
        System.out.println("Query: " + query);
        return moduleRepo.findByUniversityIdAndNameOrCode(universityId, query, query);
    }


    public boolean deleteModule(List<Long> moduleIds) {
        try {
            for (Long moduleId : moduleIds) {
                moduleRepo.deleteById(moduleId);
            }
        } catch (Exception ignored) {

         }
        return true;
    }
}
