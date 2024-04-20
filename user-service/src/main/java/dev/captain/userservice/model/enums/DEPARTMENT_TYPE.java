package dev.captain.userservice.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DEPARTMENT_TYPE {
    ACADEMIC("ACADEMIC"), NON_ACADEMIC("NON_ACADEMIC");

    private final String departmentType;
}
