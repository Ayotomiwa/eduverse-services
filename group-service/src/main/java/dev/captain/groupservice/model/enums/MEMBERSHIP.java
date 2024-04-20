package dev.captain.groupservice.model.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MEMBERSHIP {
    MEMBER("MEMBER"), MODERATOR("MODERATOR");

    private final String role;
}
