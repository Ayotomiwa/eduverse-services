package dev.captain.userservice.controller;


import dev.captain.userservice.model.SavedUserResponse;
import dev.captain.userservice.model.dto.AppUserDTO;
import dev.captain.userservice.model.dto.ProfileDTO;
import dev.captain.userservice.model.enums.AUTHORITY;
import dev.captain.userservice.model.enums.USER_TYPE;
import dev.captain.userservice.model.tables.AppUser;
import dev.captain.userservice.model.tables.ProfileInfo;
import dev.captain.userservice.model.tables.University;
import dev.captain.userservice.repo.AppAdminRepo;
import dev.captain.userservice.service.UniversityService;
import dev.captain.userservice.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-service/")
@RequiredArgsConstructor
public class UserController {

    private final UniversityService universityService;
    private final UserService userService;
    private final AppAdminRepo appAdminRepo;


    @GetMapping("users/{user-id}")
    ResponseEntity<?> getUser(@PathVariable("user-id") Long userId) {
        if (!userService.existsUserById(userId)) {
            return ResponseEntity.badRequest().body("User does not exist");
        }

        AppUser user = userService.retrieveUser(userId);
        return ResponseEntity.ok(user);
    }




    @PostMapping("university/{university-id}/admin/{admin-user-id}/users/muiltiple")
    ResponseEntity<?> createUsers(@RequestBody List<AppUserDTO> users,
                                  @PathVariable("university-id") Long universityId,
                                  @PathVariable("admin-user-id") Long adminUserId,
                                  @RequestParam Optional<String> companyID
    ) {

        if (users == null) {
            return ResponseEntity.badRequest().body("Users cannot be null");
        }

        ResponseEntity<?> response = ScreenAccess(adminUserId, universityId, companyID.orElse(null));
        if (response.getStatusCode().isError()) {
            return response;
        }

        List<SavedUserResponse.UnSavedUsers> nonAddedUsers = new ArrayList<>();
        Map<String, List<AppUserDTO>> userMap = userService.filterForExistingUsers(users);
        List<AppUserDTO> existedUsersDTO = userMap.get("existsUsers");
        List<AppUserDTO> remainingUsersDTO = userMap.get("nonExistsUsers");

        userMap = userService.filterForIncompleteUserData(remainingUsersDTO);

        List<AppUserDTO> incompleteUsersDTO = userMap.get("incompleteUsers");
        List<AppUserDTO> usersDTOToSave = userMap.get("completeUsers");


        userService.saveUsers(usersDTOToSave, universityId);

        if (!existedUsersDTO.isEmpty()) {
            for (AppUserDTO e : existedUsersDTO) {
                SavedUserResponse.UnSavedUsers unSavedUsers = new SavedUserResponse.UnSavedUsers();
                unSavedUsers.setUser(e);
                unSavedUsers.setReason("User already exists");
                nonAddedUsers.add(unSavedUsers);
            }
        }

        if (!incompleteUsersDTO.isEmpty()) {
            for (AppUserDTO u : incompleteUsersDTO) {
                SavedUserResponse.UnSavedUsers unSavedUsers = new SavedUserResponse.UnSavedUsers();
                unSavedUsers.setUser(u);
                unSavedUsers.setReason("User Data is not complete, Please check that all required fields are completed");
                nonAddedUsers.add(unSavedUsers);
            }
        }

        String saveMessage = "All users save successfully ";

        if (!nonAddedUsers.isEmpty()) {
            saveMessage = "Some user(s) were not saved successfully";
        }

        SavedUserResponse userResponse = new SavedUserResponse();
        userResponse.setUnSavedUsers(nonAddedUsers);
        userResponse.setSaveMessage(saveMessage);
        userResponse.setSavedUsersNo(usersDTOToSave.size());
        userResponse.setUnSavedUsersNo(nonAddedUsers.size());

        return ResponseEntity.ok(userResponse);
    }


    @GetMapping("users/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam String query,
            @RequestParam Long universityId,
            @RequestParam(required = false) String userType
    ) {
        if (query == null) {
            return ResponseEntity.badRequest().body("Search query cannot be null");
        }

        if (USER_TYPE.STAFF.name().equalsIgnoreCase(userType)) {
            System.out.println("Searching for staff " + userType);
            return ResponseEntity.ok(userService.getStaffAppUserDTOBySearchAndUniversity(query.toLowerCase(),
                    universityId));
        }

        if (userType != null && USER_TYPE.isValidStudentType(userType)) {
            System.out.println("Searching for student " + userType);
            List<AppUserDTO> searchResults = userService.getStudentAppUserDTOBySearchAndUserType(query.toLowerCase(),
                    universityId, List.of(USER_TYPE.STUDENT, USER_TYPE.ALUMNI));

            System.out.println("STUDENT Search results: " + searchResults);
            return ResponseEntity.ok(searchResults);
        }



        List<AppUserDTO> searchResults = userService.getAppUserDTOBySearch(query.toLowerCase(), universityId);
        return ResponseEntity.ok(searchResults);
    }


    @PostMapping("university/{university-id}/admin/{admin-user-id}/users")
    ResponseEntity<?> createUser(@RequestBody AppUserDTO userDto,
                                 @PathVariable("university-id") Long universityId,
                                 @PathVariable("admin-user-id") Long universityAdminId,
                                 @PathParam("company-id") String companyId
    ) {

        ResponseEntity<?> response = ScreenAccess(universityAdminId, universityId, companyId);

        if (response.getStatusCode().isError()) {
            return response;
        }

        if (userService.checkUserData(userDto)) {
            return ResponseEntity.badRequest().body("User Data is not complete, Please check that all required fields are completed");
        }
        if (userService.existsUser(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        AppUser savedUser = userService.create(userDto, universityId);
        if (savedUser == null) {
            return ResponseEntity.badRequest().body("User not saved");
        }
        return ResponseEntity.ok("User created");
    }

    @PostMapping("university/{university-id}/admin/{admin-user-id}/update-user")
    ResponseEntity<?> updateUser(@RequestBody AppUserDTO user,
                                 @PathVariable("university-id") Long universityId,
                                 @PathVariable("admin-user-id") Long adminUserId,
                                 @PathParam("company-id") String companyId
    ) {

        ResponseEntity<?> access = ScreenAccess(adminUserId, universityId, companyId);

        if (access.getStatusCode().isError()) {
            return access;
        }
        ResponseEntity<?> userCheck = doUserChecks(user);

        if (userCheck.getStatusCode().isError()) {
            return userCheck;
        }
        boolean hasRequiredUserData = userService.checkUserData(user);
        if (!hasRequiredUserData) {
            return ResponseEntity.badRequest().body("User Data is not complete, Please check that all required fields are completed");
        }
        userService.updateUser(user);
        return ResponseEntity.ok("User updated");
    }



    @GetMapping("/faculty/{faculty-id}")
    ResponseEntity<?> getFaculty(@PathVariable("faculty-id") Long facultyId) {
        if (facultyId == null) {
            return ResponseEntity.badRequest().body("Faculty ID cannot be null");
        }
        if (!userService.existsFacultyById(facultyId)) {
            return ResponseEntity.badRequest().body("Faculty does not exist");
        }
        return ResponseEntity.ok(userService.retrieveFaculty(facultyId));
    }

    @GetMapping("university/{university-id}/admin/{admin-user-id}/faculties")
    ResponseEntity<?> getFaculties(@PathVariable("university-id") Long universityId,
                                   @PathVariable("admin-user-id") Long adminUserId) {

        ResponseEntity<?> response = ScreenAccess(adminUserId, universityId, null);
        if (response.getStatusCode().isError()) {
            return response;
        }
        return ResponseEntity.ok(userService.retrieveFaculties(universityId));
    }


    @GetMapping("university/{university-id}/admin/{admin-user-id}/users")
    ResponseEntity<?> getUsers(@PathVariable("university-id") Long universityId,
                               @PathVariable("admin-user-id") Long adminUserId,
                               @RequestParam Optional<Integer> page,
                               @RequestParam Optional<String> sortBy,
                               @RequestParam Optional<Long> size,
                               @RequestParam Optional<String> companyID,
                               @RequestParam("user-type") Optional<String> userType
    ) {

        ResponseEntity<?> response = ScreenAccess(adminUserId, universityId, companyID.orElse(null));
        if (response.getStatusCode().isError()) {
            return response;
        }

        if (userType.isPresent() && USER_TYPE.STAFF.name().equalsIgnoreCase(userType.get())) {
            System.out.println("Staff");
            return ResponseEntity.ok(userService.retrieveStaff(
                    PageRequest.of(page.orElse(0),
                            size.orElse(30L).intValue(),
                            Sort.Direction.DESC, sortBy.orElse("id")), universityId, USER_TYPE.STAFF));
        }

        if (userType.isPresent() && USER_TYPE.isValidStudentType(userType.get())) {
            System.out.println("Student");
            return ResponseEntity.ok(userService.retrieveStudents(
                    PageRequest.of(page.orElse(0),
                            size.orElse(30L).intValue(),
                            Sort.Direction.DESC, sortBy.orElse("id")), universityId));
        }

        if (userType.isPresent() && USER_TYPE.FACULTY.name().equalsIgnoreCase(userType.get())) {
            System.out.println("Faculty");
            return ResponseEntity.ok(userService.retrieveFaculties(
                    PageRequest.of(page.orElse(0),
                            size.orElse(30L).intValue(),
                            Sort.Direction.DESC, sortBy.orElse("id")), universityId));
        }

        return ResponseEntity.ok(userService.retrieveUsers(
                PageRequest.of(page.orElse(0),
                        size.orElse(30L).intValue(),
                        Sort.Direction.DESC, sortBy.orElse("id")), universityId));
    }


    @GetMapping("users/{user-id}/profile")
    ResponseEntity<?> getUserProfile(@PathVariable("user-id") Long userId) {
        if (!userService.existsUserById(userId)) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        ProfileDTO userProfile = userService.retrieveUserProfile(userId);

        if (userProfile == null) {
            return ResponseEntity.badRequest().body("User profile does not exist");
        }

        return ResponseEntity.ok(userProfile);
    }


    @PostMapping("user/{user-id}/profile")
    ResponseEntity<?> updateUserProfile(@RequestBody ProfileInfo profileInfo,
                                        @PathVariable("user-id") Long userId) {
        if (!userService.existsUserById(userId)) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        if (profileInfo == null) {
            return ResponseEntity.badRequest().body("Profile info cannot be null");
        }
        userService.updateUserProfile(profileInfo);
        return ResponseEntity.ok("Profile updated");
    }


    public ResponseEntity<?> ScreenAccess(Long userId, long universityId, String companyID) {

        boolean existsUniversity = universityService.existsUniversity(universityId);
        if (!existsUniversity) {
            return ResponseEntity.badRequest().body("University does not exist");
        }

        University university = universityService.retrieveUniversity(universityId);

        if (!university.isVerified()) {
            return ResponseEntity.badRequest().body("University has not verified, please contact support");
        }

        if (companyID != null && appAdminRepo.existsById(companyID)) {
            return ResponseEntity.ok("User has access to the university");
        }


        AppUser adminUser = userService.retrieveUser(userId);
        if (adminUser == null) {
            return ResponseEntity.badRequest().body("Admin user does not exist");
        }
        if (!adminUser.getUniversity().getId().equals(universityId)) {
            return ResponseEntity.badRequest().body("Admin user does not belong to this university");
        }

        boolean isAdmin = adminUser.getAuthority().equals(AUTHORITY.ADMIN);

        if (!isAdmin) {
            return ResponseEntity.badRequest().body("Admin user does not have the required role");
        }

        return ResponseEntity.ok("User has access to the university");
    }


    public ResponseEntity<?> doUserChecks(AppUserDTO user) {

        if (user == null) {
            return ResponseEntity.badRequest().body("User cannot be null");
        }

        if (!userService.existsUserById(user.getId())) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
        return ResponseEntity.ok("User checks passed");
    }

    @GetMapping("users/{user-id}/authority")
    public ResponseEntity<?> getAuthority(@PathVariable("user-id") Long userId) {
        return ResponseEntity.ok(userService.getUserAuthority(userId));
    }


}
