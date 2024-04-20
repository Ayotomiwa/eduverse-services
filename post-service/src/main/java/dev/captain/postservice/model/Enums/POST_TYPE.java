package dev.captain.postservice.model.Enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum POST_TYPE {

    POLL("POLL"), POST("POST"), ANNOUNCEMENT("ANNOUNCEMENT");

    private final String value;
}
