package dev.captain.userservice.model.enums;


import lombok.Getter;

@Getter
public enum AUTHORITY {
    ADMIN("ADMIN"), STANDARD("STANDARD"), ELEVATED("ELEVATED");

    private final String role;

    AUTHORITY(String role) {
        this.role = role;
    }
}
