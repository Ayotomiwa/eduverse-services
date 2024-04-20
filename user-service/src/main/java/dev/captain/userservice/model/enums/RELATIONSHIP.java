package dev.captain.userservice.model.enums;


import lombok.Getter;

@Getter
public enum RELATIONSHIP {
    FRIEND("FRIEND"), BLOCKED("BLOCKED"), PENDING("PENDING");

    private final String relationship;

    RELATIONSHIP(String relationship) {
        this.relationship = relationship;
    }
}
