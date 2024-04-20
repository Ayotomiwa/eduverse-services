package dev.captain.userservice.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum USER_TYPE {
    ALUMNI("ALUMNI"), STUDENT("STUDENT"), STAFF("STAFF"), FACULTY("FACULTY"), ALL("ALL");

    private final String userType;


    public static boolean isValidType(String type) {
        for (USER_TYPE userType : USER_TYPE.values()) {
            if (userType.getUserType().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }


    public static boolean isValidStudentType(String type) {
        return isValidType(type) && (type.equalsIgnoreCase(USER_TYPE.STUDENT.getUserType()) || type.equalsIgnoreCase(USER_TYPE.ALUMNI.getUserType()));
    }


    public static boolean isValidStaffType(String type) {
        return !isValidStudentType(type);
    }

}
