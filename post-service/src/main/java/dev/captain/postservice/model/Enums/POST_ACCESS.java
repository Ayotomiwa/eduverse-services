package dev.captain.postservice.model.Enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum POST_ACCESS {

    PUBLIC("PUBLIC"), PROTECTED("PROTECTED"), STANDARD("STANDARD");

    private final String value;
}