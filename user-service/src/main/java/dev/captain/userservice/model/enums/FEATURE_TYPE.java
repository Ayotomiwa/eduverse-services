package dev.captain.userservice.model.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FEATURE_TYPE {
    CONTENT("CONTENT"), GROUP("GROUP"), EVENT("EVENT"), MODULE("MODULE");

    private final String type;


}
