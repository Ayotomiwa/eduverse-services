package dev.captain.userservice.service;

import com.mailjet.client.errors.MailjetException;
import dev.captain.userservice.model.dto.AppUserDTO;
import dev.captain.userservice.model.dto.ProfileDTO;
import dev.captain.userservice.model.dto.StudentDTO;
import dev.captain.userservice.model.enums.AUTHORITY;
import dev.captain.userservice.model.enums.STAFF_TYPE;
import dev.captain.userservice.model.enums.USER_TYPE;
import dev.captain.userservice.model.tables.*;
import dev.captain.userservice.repo.*;
import dev.captain.userservice.service.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;


import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ProfileInfoRepo profileInfoRepo;
    private final UniversityRepo universityRepo;
    private final CourseRepo courseRepo;
    private final StudentRepo studentRepo;
    private final FacultyRepo facultyRepo;

    private final PasswordEncoder bCryptPasswordEncoder;
    private final DepartmentRepo departmentRepo;
    private final MailService mailService;
    private final StaffRepo staffRepo;


    public String saveUsers(List<AppUserDTO> userDTOS, Long universityId) {

        University university = universityRepo.findById(universityId).get();
        List<AppUser> users = new ArrayList<>();

        for (AppUserDTO userDTO : userDTOS) {
            users.add(create(userDTO, universityId));
        }
        if (university.getUsers() == null) {
            university.setUsers(new ArrayList<>());
        }
        university.getUsers().addAll(users);
        universityRepo.save(university);

        return "Users saved successfully";
    }


    public Map<String, List<AppUserDTO>> filterForExistingUsers(List<AppUserDTO> users) {
        List<AppUserDTO> existsUsers = new ArrayList<>();

        users.removeIf(user -> {
            if (existsUser(user.getEmail())) {
                existsUsers.add(user);
                return true;
            }
            return false;
        });

        return Map.of("existsUsers", existsUsers, "nonExistsUsers", users);
    }

    public Map<String, List<AppUserDTO>> filterForIncompleteUserData(List<AppUserDTO> users) {
        List<AppUserDTO> incompleteUsers = new ArrayList<>();

        for (AppUserDTO user : users) {
            if (user.getEmail() == null) {
                boolean incompleteData = checkUserData(user);
                if (incompleteData) {
                    incompleteUsers.add(user);
                }
            }
        }
        return Map.of("incompleteUsers", incompleteUsers, "completeUsers", users);
    }


    public boolean checkUserData(AppUserDTO user) {
        return AppUser.checkRequiredData(UserMapper.convertToUserFromDto(user));
    }

    public boolean existsUser(String email) {
        return userRepo.existsAppUserByEmail(email);
    }

    public AppUser updateUser(AppUserDTO userDto) {
        AppUser appUser = userRepo.findById(userDto.getId()).orElse(null);
        if (appUser == null) {
            return null;
        }
        appUser.setFirstName(userDto.getFirstName());
        appUser.setLastName(userDto.getLastName());
        appUser.setEmail(userDto.getEmail());
        appUser.setAuthority(userDto.getAuthority());
        appUser.setUserType(USER_TYPE.valueOf(userDto.getUserType()));

        if (userDto.getStaff() != null) {
            Staff staff = appUser.getStaff();
            staff.setStaffNumber(userDto.getStaff().getStaffNumber());
            staff.setStaffType(STAFF_TYPE.valueOf(userDto.getStaff().getStaffType()));
            staff.setDepartment(getStaffDepartment(userDto.getStaff().getDepartment(), appUser.getUniversity().getId()));
        }

        if (userDto.getStudent() != null) {
            Student student = appUser.getStudent();
            student.setStudentNumber(userDto.getStudent().getStudentNumber());
            student.setCourse(getStudentCourse(userDto.getStudent(), appUser.getUniversity().getId()));
            student.setStartYear(userDto.getStudent().getStartYear());
        } else if (userDto.getFaculty() != null) {
            Faculty faculty = appUser.getFaculty();
            faculty.setName(userDto.getFaculty().getName());
            if (userDto.getFaculty().getDepartment() != null) {
                faculty.setDepartment(getStaffDepartment(userDto.getFaculty().getDepartment(), appUser.getUniversity().getId()));
            }
        }

        appUser.setModifiedAt(LocalDateTime.now());
        return userRepo.save(appUser);
    }

    public AppUser create(AppUserDTO userDTO, Long universityId) {
        AppUser appUser = UserMapper.convertToUserFromDto(userDTO);
        appUser.setModifiedAt(LocalDateTime.now());


        University university = universityRepo.findById(universityId).get();

        appUser.setUniversity(university);
        appUser.setActive(true);
        appUser.setBanned(false);
        appUser.setUsername(userDTO.getEmail().split("@")[0]);
        if (appUser.getAuthority() == null) {
            appUser.setAuthority(AUTHORITY.STANDARD);
        }

        ProfileInfo profileInfo = new ProfileInfo();
        profileInfo.setUser(appUser);


        profileInfo.setCoverPicUrl(null);
        profileInfo.setProfilePicUrl(null);
        appUser.setProfileInfo(profileInfo);
        appUser.setModifiedAt(LocalDateTime.now());

        boolean isStudent = USER_TYPE.isValidStudentType(userDTO.getUserType());
        boolean isStaff = USER_TYPE.isValidStaffType(userDTO.getUserType());

        System.out.println("isStudent: " + isStudent + " isStaff: " + isStaff);

        if (isStudent && userDTO.getStudent() != null) {
            Student student = new Student();
            student.setUser(appUser);
            student.setCourse(getStudentCourse(userDTO.getStudent(), universityId));
            student.setStartYear(userDTO.getStudent().getStartYear());
            student.setStudentNumber(userDTO.getStudent().getStudentNumber());
            if (student.getCourse() != null) {
                if (appUser.getUserType() == USER_TYPE.STUDENT) {
                    int year = Year.now().getValue() - student.getStartYear();
                    profileInfo.setBio(year + "th year student of " + appUser.getStudent().getCourse().getCourseName());
                } else if (appUser.getUserType() == USER_TYPE.ALUMNI) {
                    int year = (int) (student.getStartYear() + appUser.getStudent().getCourse().getDuration());
                    profileInfo.setBio(year + " Alumni of " + appUser.getStudent().getCourse().getCourseName());
                } else {
                    profileInfo.setBio("User from" + appUser.getUniversity().getName());
                }
            } else {
                profileInfo.setBio("Student at " + appUser.getUniversity().getName());
            }
            appUser.setStudent(student);
        } else if (isStaff && userDTO.getStaff() != null) {
            Staff staff = new Staff();
            staff.setUser(appUser);
            staff.setStaffNumber(userDTO.getStaff().getStaffNumber());
            staff.setStaffType(STAFF_TYPE.valueOf(userDTO.getStaff().getStaffType()));
            if (userDTO.getStaff().getDepartment() != null) {
                staff.setDepartment(getStaffDepartment(userDTO.getStaff().getDepartment(), universityId));
            }
            if (staff.getDepartment() != null) {
                profileInfo.setBio(staff.getStaffType().name() + " at " + staff.getDepartment().getDepartmentName());
            } else {
                profileInfo.setBio("Staff at " + appUser.getUniversity().getName());
            }
            appUser.setStaff(staff);
        }

        if (userDTO.getFaculty() != null) {

            Faculty faculty = new Faculty();
            faculty.setUserAccount(appUser);
            faculty.setName(userDTO.getFaculty().getName());
            if (userDTO.getFaculty().getDepartment() != null) {
                faculty.setDepartment(getStaffDepartment(userDTO.getFaculty().getDepartment(), universityId));
                if (faculty.getDepartment() != null) {
                    profileInfo.setBio(faculty.getName() + " at " + faculty.getDepartment().getDepartmentName());
                } else {
                    profileInfo.setBio(faculty.getName() + "-" + appUser.getUniversity().getName());
                }
            } else {
                profileInfo.setBio(faculty.getName() + " at " + appUser.getUniversity().getName());
            }
            appUser.setFirstName(userDTO.getFaculty().getName());
            appUser.setFaculty(faculty);
        }

//
//        String generateRandomPassword = new Random().ints(48, 123)
//                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                .limit(8)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                .toString();
//


//            Context context = new Context();
//            context.setVariable("universityName", university.getName());
//            context.setVariable("recipientName", user.getFirstName() + " " + user.getLastName());
//            context.setVariable("password", generateRandomPassword);

//            try {
//                mailService.sendEmail(user.getEmail(), "Account Creation", context, "Eduverse");
//            } catch (MailjetException e) {
//                throw new RuntimeException(e);
//            }
        if (appUser.getPassword() == null || appUser.getPassword().isEmpty()) {

            String generateRandomPassword = generatePassword();


            Context context = new Context();
            context.setVariable("universityName", university.getName());
            context.setVariable("recipientName", appUser.getFirstName() + " " + appUser.getLastName());
            context.setVariable("password", generateRandomPassword);
            context.setVariable("signinLink", "https://eduverse-4hic.vercel.app/login");

            try {
                mailService.sendEmail(appUser.getEmail(), "Account Creation", context, "Eduverse");
            } catch (MailjetException e) {
                throw new RuntimeException(e);
            }


            appUser.setPassword(bCryptPasswordEncoder.encode("12345"));
        } else {
            appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        }

        return userRepo.save(appUser);
    }


    public String generatePassword(){

        String generateRandomPassword = new Random().ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(8)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

       return generateRandomPassword;
    }



    public AppUser retrieveUser(Long userId) {
        return userRepo.findAppUserById((userId));
    }

    public boolean existsUserById(Long userId) {
        return userRepo.existsAppUserById(userId);
    }


    public ProfileDTO retrieveUserProfile(Long userId) {
        AppUser user = userRepo.findById(userId).orElse(null);
        ProfileDTO profileDTO = new ProfileDTO();
        if (user != null && user.getProfileInfo() != null) {
            profileDTO.setBio(user.getProfileInfo().getBio());
            profileDTO.setCoverPicUrl(user.getProfileInfo().getCoverPicUrl());
            profileDTO.setProfilePicUrl(user.getProfileInfo().getProfilePicUrl());
            profileDTO.setUserId(user.getId());
            profileDTO.setFirstName(user.getFirstName());
            profileDTO.setLastName(user.getLastName());
            if(user.getFollowedUsers() != null) {
                profileDTO.setFollowersCount((long) user.getFollowers().size());
            }else{
                profileDTO.setFollowersCount(0L);
            }
            if(user.getFollowedUsers() != null) {
                profileDTO.setFollowingCount((long) user.getFollowedUsers().size());
            }
            else{
                profileDTO.setFollowingCount(0L);
            }
            if (user.getUserType() == USER_TYPE.FACULTY) {
                profileDTO.setFacultyName(user.getFaculty().getName());
                profileDTO.setFacultyId(user.getFaculty().getId());
            }
        }
        return profileDTO;
    }

    public void updateUserProfile(ProfileInfo profileInfo) {
        profileInfoRepo.save(profileInfo);
    }


    public Page<AppUserDTO> retrieveUsers(PageRequest page, Long universityId) {
        Page<AppUser> usersPage = userRepo.findAllByUniversityId(page, universityId);
        return usersPage.map(UserMapper::convertUserToDto);
    }


    public List<AppUserDTO> retrieveUsers(Long universityId) {

        List<AppUser> users = userRepo.findAllByUniversityId(universityId);
        return UserMapper.convertUsersToDto(users);
    }

    public Page<AppUserDTO> retrieveStaff(PageRequest id, Long universityId, USER_TYPE userType) {
        Page<AppUser> usersPage = userRepo.findAllByUserTypeAndUniversityId(id, userType, universityId);
        System.out.println(usersPage.toString());
        return usersPage.map(UserMapper::convertUserToDto);
    }

    public Page<AppUserDTO> retrieveStudents(PageRequest page, Long universityId) {
        Page<AppUser> usersPage = userRepo.findAllByUniversityIdAndUserTypeOrUserType(page, universityId, USER_TYPE.STUDENT, USER_TYPE.ALUMNI);
        System.out.println(usersPage.map(UserMapper::convertUserToDto).getContent());
        return usersPage.map(UserMapper::convertUserToDto);
    }

    public Page<AppUserDTO> retrieveFaculties(PageRequest id, Long universityId) {
        Page<AppUser> usersPage = userRepo.findAllByUserTypeAndUniversityId(id, USER_TYPE.FACULTY, universityId);
        System.out.println(usersPage.toString());
        return usersPage.map(UserMapper::convertUserToDto);
    }

    public List<AppUserDTO> retrieveFaculties(Long universityId) {
        return userRepo.findByUniversityIdAndUserType(universityId, USER_TYPE.FACULTY).stream().map(UserMapper::convertUserToDto).toList();
    }


    public Course getStudentCourse(StudentDTO student, Long universityId) {
        return courseRepo.findByDepartmentUniversityIdAndCourseCodeOrCourseName(
                universityId, student.getCourse(), student.getCourse()
        );
    }


    public Department getStaffDepartment(String department, Long universityId) {
        return departmentRepo.findByUniversityIdAndDepartmentCodeOrDepartmentName(
                universityId, department, department
        );
    }

    public List<AppUserDTO> getAppUserDTOBySearch(String query, Long universityId) {
        String queryParameter = "%" + query + "%";
        return UserMapper.convertUsersToDto(userRepo.findByUniversityIdAndNameOrEmail(universityId, queryParameter, queryParameter));
    }

    public List<AppUserDTO> getStudentAppUserDTOBySearchAndUserType(String lowerCase, Long universityId, List<USER_TYPE> userTypes) {
        String queryParameter = "%" + lowerCase + "%";
        System.out.println("Searching for students with query: " + queryParameter + " and universityId: " + universityId + " and userTypes: " + userTypes);

        List<AppUser> users = userRepo.findByUniversityIdAndStudentOrAlumniAndNameOrEmail(
                universityId, USER_TYPE.STUDENT, USER_TYPE.ALUMNI, queryParameter, queryParameter);

        List<AppUserDTO> userDTOS = UserMapper.convertUsersToDto(users);
        System.out.println("Users Found " + userDTOS);
        return userDTOS;
    }

    public String getUserAuthority(Long userId) {
        return userRepo.findById(userId).get().getAuthority().name();
    }

    public String getUserType(Long userId) {
        return userRepo.findById(userId).get().getUserType().name();
    }

    public Faculty retrieveFaculty(Long facultyId) {
        return facultyRepo.findById(facultyId).orElse(null);
    }

    public boolean existsFacultyById(Long facultyId) {
        return facultyRepo.existsById(facultyId);
    }


    public List<AppUserDTO> getStaffAppUserDTOBySearchAndUniversity(String lowerCase, Long universityId) {
        String queryParameter = "%" + lowerCase + "%";
       List<AppUser> users = userRepo.findByUniversityIdAndStaffAndNameOrEmail(universityId, USER_TYPE.STAFF, queryParameter, queryParameter);


       List<AppUserDTO> userDTOS = UserMapper.convertUsersToDto(users);
        System.out.println("Users Found " + userDTOS);

        return userDTOS;
    }


    public List<AppUserDTO> retrieveUsers(List<Long> userIds) {
        List<AppUser> users = staffRepo.findAllById(userIds).stream().map(staff -> staff.getUser()).toList();

        return UserMapper.convertUsersToDto(users);
    }
}

