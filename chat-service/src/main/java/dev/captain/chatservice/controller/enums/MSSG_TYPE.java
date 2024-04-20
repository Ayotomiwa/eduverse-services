package dev.captain.chatservice.controller.enums;


import lombok.Getter;

@Getter
public enum MSSG_TYPE {
    CHAT("CHAT"), JOIN("JOIN"), LEAVE("JOIN");
    private final String type;

    MSSG_TYPE(String type) {
        this.type = type;
    }
}
