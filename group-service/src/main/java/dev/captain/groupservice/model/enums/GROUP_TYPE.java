package dev.captain.groupservice.model.enums;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum GROUP_TYPE {
    ALUMNI("ALUMNI"), FACULTY("FACULTY"), STUDENT("STUDENT");

    private final String type;

    GROUP_TYPE(String type) {
        this.type = type;
    }
}
