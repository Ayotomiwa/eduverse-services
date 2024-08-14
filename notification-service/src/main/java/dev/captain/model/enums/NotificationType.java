package dev.captain.model.enums;

import lombok.Setter;


public enum NotificationType {

    DISCUSSION("discussion"),
    POST("post"),
    COMMENT("comment"),
    EVENT("event"),
    CHAT("chat");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }


    public static boolean isValidType(String type) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (!notificationType.type.equalsIgnoreCase(type)) {
                return false;
            }
        }
        return true;
    }
}
