package dev.captain.userservice.controller;

import dev.captain.userservice.model.enums.AUTHORITY;
import dev.captain.userservice.model.enums.USER_TYPE;
import dev.captain.userservice.model.tables.*;
import dev.captain.userservice.model.tables.Module;
import dev.captain.userservice.repo.CourseRepo;
import dev.captain.userservice.repo.DepartmentRepo;
import dev.captain.userservice.service.ModuleService;
import dev.captain.userservice.service.UniversityService;
import dev.captain.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user-service/")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;
    private final UniversityService universityService;
    private final UserService userService;
//    private final CourseRepo courseRepo;
//    private final DepartmentRepo departmentRepo;

    @GetMapping("university/{university-id}/modules")
    public ResponseEntity<?> getModules(@PathVariable("university-id") Long universityId) {
        return ResponseEntity.ok(moduleService.getModules(universityId));
    }


    @GetMapping("university/{university-id}/courses")
    public ResponseEntity<?> getCourses(@PathVariable("university-id") Long universityId){

//        List<Department> departments = departmentRepo.findByUniversityId(universityId);
//        List<Course> course = departments.stream().map(department -> {
//            return department.getCourses().stream().toList();
//        }).reduce();
//
//        return courseRepo.findByUniversityId(universityId);
        return null;
    }


    @PostMapping("university/{university-id}/modules")
    public ResponseEntity<?> addModule(@RequestBody Module module, @PathVariable("university-id") Long universityId, @RequestParam("user-id") Long userId) {
        if (module == null) return ResponseEntity.badRequest().body("Module cannot be null");
        if (module.getName() == null || module.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Module name cannot be null or empty");
        }
        ResponseEntity<?> access = ScreenAccess(userId, universityId);
        if (access.getStatusCode().isError()) {
            return access;
        }

        if (module.getProfileUrl() != null && !module.getProfileUrl().startsWith("http")) {
            String logoUrl = "https://eduverse-v1.s3.eu-west-2.amazonaws.com/" + universityId + "/" + module.getProfileUrl();
            module.setProfileUrl(logoUrl);
        }

        return ResponseEntity.ok(moduleService.createModule(module, universityId));
    }

    @GetMapping("modules/{module-id}")
    public ResponseEntity<?> getModule(@PathVariable("module-id") Long moduleId) {
        if (moduleId == null) return ResponseEntity.badRequest().body("Module id cannot be null");
        if (!moduleService.existsModule(moduleId)) {
            return ResponseEntity.badRequest().body("Module does not exist");
        }
        return ResponseEntity.ok(moduleService.getModule(moduleId));
    }


    @GetMapping("/modules/search")
    public ResponseEntity<?> searchModule(@RequestParam Long universityId, @RequestParam String query) {
        if (query == null) {
            return ResponseEntity.badRequest().body("Search query cannot be empty");
        }
        return ResponseEntity.ok(moduleService.searchModule(universityId, query));
    }

    @GetMapping("users/{user-id}/modules")
    public ResponseEntity<?> getModuleByUser(@PathVariable("user-id") Long userId) {
        if (userId == null) return ResponseEntity.badRequest().body("User id cannot be null");
        if (!userService.existsUserById(userId)) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        return ResponseEntity.ok(moduleService.getModuleByUser(userId));
    }


    @PostMapping("modules/{module-id}/student/{student-id}")
    public ResponseEntity<?> addStudentToModule(@PathVariable("module-id") Long moduleId, @PathVariable("student-id") Long studentId,
                                                @RequestParam("user-id") Long userId) {
        ResponseEntity<?> access = ScreenModuleAccess(userId, studentId, moduleId, "Student");
        if (access.getStatusCode().isError()) {
            return access;
        }
        return ResponseEntity.ok(moduleService.addStudentToModule(moduleId, studentId));
    }

    @PostMapping("modules/{module-id}/student/multiple")
    public ResponseEntity<?> addMultipleStudentToModule(@PathVariable("module-id") Long moduleId, @RequestBody List<Long> studentIds,
                                                        @RequestParam Long userId) {
        ResponseEntity<?> access = null;
        for (Long studentId : studentIds) {
            access = ScreenModuleAccess(userId, studentId, moduleId, "Student");
            if (access.getStatusCode().isError()) {
                return access;
            }
        }
        return ResponseEntity.ok(moduleService.addStudentsToModule(moduleId, studentIds));
    }

    @PostMapping("modules/{module-id}/staff/multiple")
    public ResponseEntity<?> addMultipleStaffToModule(@PathVariable("module-id") Long moduleId, @RequestBody List<Long> staffIds,
                                                      @RequestParam Long userId) {
        ResponseEntity<?> access = null;
        for (Long staffId : staffIds) {
            access = ScreenModuleAccess(userId, staffId, moduleId, "Staff");
            if (access.getStatusCode().isError()) {
                return access;
            }
        }
        return ResponseEntity.ok(moduleService.addStaffToModule(moduleId, staffIds));
    }


    @PostMapping("modules/{module-id}/staff/{staff-id}")
    public ResponseEntity<?> addStaffToModule(@PathVariable("module-id") Long moduleId, @PathVariable("staff-id") Long staffId,
                                              @RequestParam Long userId) {
        ResponseEntity<?> access = ScreenModuleAccess(userId, staffId, moduleId, "Staff");
        if (access.getStatusCode().isError()) {
            return access;
        }
        return ResponseEntity.ok(moduleService.addStaffToModule(moduleId, staffId));
    }


    @GetMapping("modules/{module-id}/authorized")
    public ResponseEntity<?> isAuthorized(@PathVariable("module-id") Long moduleId, @RequestParam Long userId) {

        if (!moduleService.existsModule(moduleId)) {
            return ResponseEntity.badRequest().body("Module does not exist");
        }

        if (!Objects.equals(userService.getUserAuthority(userId), AUTHORITY.ADMIN.name())
                && !Objects.equals(userService.getUserType(userId), USER_TYPE.STAFF.name())) {
            return ResponseEntity.badRequest().body("User does not authorized to perform this operation");
        }
        return ResponseEntity.ok("ACCESS GRANTED");
    }


    public ResponseEntity<?> ScreenModuleAccess(Long moduleId, Long userTypeID, Long userId, String userType) {
        if (moduleId == null) return ResponseEntity.badRequest().body("Module id cannot be null");
        if (userTypeID == null) return ResponseEntity.badRequest().body("Staff/Student id cannot be null");
        if (!moduleService.existsModule(moduleId)) {
            return ResponseEntity.badRequest().body("Module does not exist");
        }
        if (userType.equals("Staff")) {
            if (!universityService.existsStaff(userTypeID)) {
                return ResponseEntity.badRequest().body("Staff does not exist");
            }
        } else {
            if (!universityService.existsStudent(userTypeID)) {
                return ResponseEntity.badRequest().body("Student does not exist");
            }
        }

        if (userId == null) {
            return ResponseEntity.badRequest().body("User id cannot be null");
        }
        if (!Objects.equals(userService.getUserAuthority(userId), AUTHORITY.ADMIN.name())
                && !Objects.equals(userService.getUserType(userId), USER_TYPE.STAFF.name())) {
            return ResponseEntity.badRequest().body("User does not authorized to perform this operation");
        }

        return ResponseEntity.ok("Access granted");

    }


    public ResponseEntity<?> ScreenAccess(Long userId, long universityId) {

        boolean existsUniversity = universityService.existsUniversity(universityId);
        if (!existsUniversity) {
            return ResponseEntity.badRequest().body("University does not exist");
        }

        University university = universityService.retrieveUniversity(universityId);

        if (!university.isVerified()) {
            return ResponseEntity.badRequest().body("University has not verified, please contact support");
        }

        AppUser user = userService.retrieveUser(userId);

        if (user == null) {
            return ResponseEntity.badRequest().body("user does not exist");
        }

        if (!user.getUniversity().getId().equals(universityId)) {
            return ResponseEntity.badRequest().body("User does not belong to this university");
        }

        boolean isAdmin = user.getAuthority().equals(AUTHORITY.ADMIN);
        boolean isStaff = user.getUserType().equals(USER_TYPE.STAFF);

        if (!isAdmin && !isStaff) {
            return ResponseEntity.badRequest().body("User does not have the required role");
        }

        return ResponseEntity.ok("User has access");
    }

    @PostMapping("/modules/delete")
    public ResponseEntity<?> deleteModule(@RequestBody List<Long> moduleIds, @RequestParam("user-id") Long userId) {
        if (moduleIds == null || moduleIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Module name cannot be null or empty");
        }
        if (userId == null) {
            return ResponseEntity.badRequest().body("User id cannot be null");
        }
        if (!Objects.equals(userService.getUserAuthority(userId), "ADMIN")) {
            return ResponseEntity.badRequest().body("User does not authorized to perform this operation");
        }

        moduleService.deleteModule(moduleIds);
        return ResponseEntity.ok("Modules deleted successfully");
    }


}
