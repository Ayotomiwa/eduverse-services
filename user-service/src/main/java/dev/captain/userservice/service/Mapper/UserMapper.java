package dev.captain.userservice.service.Mapper;

import dev.captain.userservice.model.dto.AppUserDTO;
import dev.captain.userservice.model.dto.FacultyDTO;
import dev.captain.userservice.model.dto.StaffDTO;
import dev.captain.userservice.model.dto.StudentDTO;
import dev.captain.userservice.model.enums.USER_TYPE;
import dev.captain.userservice.model.tables.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class UserMapper {

    public static AppUserDTO convertUserToDto(AppUser appUser) {

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(appUser.getId());
        appUserDTO.setFirstName(appUser.getFirstName());
        appUserDTO.setLastName(appUser.getLastName());
        appUserDTO.setProfilePicUrl(appUser.getProfileInfo().getProfilePicUrl());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setUserType(appUser.getUserType().name());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setAuthority(appUser.getAuthority());
        if (appUser.getStaff() != null) {
            appUserDTO.setStaff(new StaffDTO());
            appUserDTO.getStaff().setStaffNumber(appUser.getStaff().getStaffNumber());
            if (appUser.getStaff().getDepartment() != null) {
                appUserDTO.getStaff().setDepartment(appUser.getStaff().getDepartment().getDepartmentCode());
            }
            appUserDTO.getStaff().setStaffType(appUser.getStaff().getStaffType().name());
        }
        if (appUser.getStudent() != null) {
            appUserDTO.setStudent(new StudentDTO());
            appUserDTO.getStudent().setStudentNumber(appUser.getStudent().getStudentNumber());
            if (appUser.getStudent().getCourse() != null) {
                appUserDTO.getStudent().setCourse(appUser.getStudent().getCourse().getCourseCode());
            }
            appUserDTO.getStudent().setStartYear(appUser.getStudent().getStartYear());
        }
        if (appUser.getFaculty() != null) {
            appUserDTO.setFaculty(new FacultyDTO());
            appUserDTO.getFaculty().setId(appUser.getFaculty().getId());
            appUserDTO.getFaculty().setName(appUser.getFaculty().getName());
            if (appUser.getFaculty().getDepartment() != null) {
                appUserDTO.getFaculty().setDepartment(appUser.getFaculty().getDepartment().getDepartmentCode());
            }
        }
        return appUserDTO;
    }

    public static AppUser convertToUserFromDto(AppUserDTO appUserDto) {
        if (appUserDto == null) {
            return null;
        }
        AppUser appUser = new AppUser();
        appUser.setId(appUserDto.getId());
        appUser.setFirstName(appUserDto.getFirstName());
        appUser.setLastName(appUserDto.getLastName());
        appUser.setEmail(appUserDto.getEmail());
        appUser.setUsername(appUserDto.getUsername());
        appUser.setAuthority(appUserDto.getAuthority());
        appUser.setPassword(appUserDto.getPassword());
        String userType = appUserDto.getUserType();
        if (userType != null && USER_TYPE.isValidType(userType)) {
            appUser.setUserType(USER_TYPE.valueOf(appUserDto.getUserType()));
        }
        return appUser;
    }


    public static List<AppUser> convertToUsersFromDto(List<AppUserDTO> appUserDTOS) {
        if (appUserDTOS.isEmpty()) {
            return new ArrayList<>();
        }
        List<AppUser> appUsers = new ArrayList<>();

        for (AppUserDTO dto : appUserDTOS) {
            AppUser appUser = new AppUser();
            appUser.setId(dto.getId());
            appUser.setUsername(dto.getUsername());
            appUser.setFirstName(dto.getFirstName());
            appUser.setLastName(dto.getLastName());
            appUser.setAuthority(dto.getAuthority());
            appUser.setUserType(USER_TYPE.valueOf(dto.getUserType()));
            appUser.setEmail(dto.getEmail());
            appUsers.add(appUser);
        }
        return appUsers;
    }


    public static List<AppUserDTO> convertUsersToDto(List<AppUser> appUsers) {
        if (appUsers.isEmpty()) {
            return new ArrayList<>();
        }
        List<AppUserDTO> appUserDTOS = new ArrayList<>();

        for (AppUser appUser : appUsers) {
            AppUserDTO appUserDTO = convertUserToDto(appUser);
            appUserDTOS.add(appUserDTO);
        }
        return appUserDTOS;
    }

    public List<Student> convertToStudentsFromDto(List<AppUserDTO> usersDTOToSave) {
        if (usersDTOToSave.isEmpty()) {
            return new ArrayList<>();
        }
        List<Student> students = new ArrayList<>();
        for (AppUserDTO userDTO : usersDTOToSave) {
            Student student = new Student();
            if (Objects.equals(userDTO.getUserType(), "STUDENT")) {
                student.setUser(convertToUserFromDto(userDTO));
                student.setStudentNumber(userDTO.getStudent().getStudentNumber());
                student.setStartYear(userDTO.getStudent().getStartYear());
                student.setCourse(new Course());
                student.getCourse().setCourseName(userDTO.getStudent().getCourse());
                student.setUser(convertToUserFromDto(userDTO));
                students.add(student);
            }

        }
        return students;
    }

    public List<Staff> convertToStaffFromDto(List<AppUserDTO> usersDTOToSave) {
        if (usersDTOToSave.isEmpty()) {
            return new ArrayList<>();
        }
        List<Staff> staffs = new ArrayList<>();
        for (AppUserDTO userDTO : usersDTOToSave) {
            Staff staff = new Staff();
            if (Objects.equals(userDTO.getUserType(), "STAFF")) {
                staff.setUser(convertToUserFromDto(userDTO));
                staff.setStaffNumber(userDTO.getStaff().getStaffNumber());
                staff.setDepartment(new Department());
                staff.getDepartment().setDepartmentName(userDTO.getStaff().getDepartment());
                staff.setUser(convertToUserFromDto(userDTO));
                staffs.add(staff);
            }

        }
        return staffs;
    }
}
