package dev.captain.eventservice.model.enums;

import lombok.Getter;

@Getter
public enum EVENT_TYPE {
    GROUP("GROUP"), MODULE("MODULE");

    private final String type;

    EVENT_TYPE(String type) {
        this.type = type;
    }
}
