package dev.captain.userservice.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum STAFF_TYPE {
    TEACHING("TEACHING"), NON_TEACHING("NON_TEACHING"), MANAGEMENT("MANAGEMENT"),
    LECTURER("LECTURER"), PROFESSOR("PROFESSOR"), FACULTY("FACULTY");


    private final String staffType;
}
