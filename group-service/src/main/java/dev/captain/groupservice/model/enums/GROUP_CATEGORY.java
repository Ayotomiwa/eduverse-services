package dev.captain.groupservice.model.enums;


import lombok.Getter;

@Getter
public enum GROUP_CATEGORY {
    SOCIAL("SOCIAL"), SPORTS("SPORTS"), CULTURAL("CULTURAL"), OTHER("OTHER"), CAREER("CAREER"), TECHNOLOGY("TECHNOLOGY"),
    ALUMNI("ALUMNI"), NETWORKING("NETWORKING");

    private final String category;

    GROUP_CATEGORY(String category) {
        this.category = category;
    }
}
