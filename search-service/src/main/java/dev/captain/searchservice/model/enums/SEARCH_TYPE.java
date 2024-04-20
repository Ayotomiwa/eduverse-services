package dev.captain.searchservice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SEARCH_TYPE {

    GROUP("GROUP"), USER("USER"), MODULE("MODULE");

    private final String searchType;
}
