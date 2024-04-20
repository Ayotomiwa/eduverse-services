package dev.captain.userservice.model.enums;

import lombok.Getter;


@Getter
public enum FEATURE {
    CONTENT_POSTING("CONTENT_POSTING"),
    CONTENT_FEED("CONTENT_FEED"),
    PRIVATE_MESSAGING("PRIVATE_MESSAGING"),
    EVENTS("EVENTS"),
    MODULE("MODULE"),
    POST_COMMENTING("POST_COMMENTING"),
    GROUP("GROUP");

    private final String featureName;

    FEATURE(String feature) {
        this.featureName = feature;
    }


    public static String getFeatureDescription(FEATURE feature) {
        return switch (feature) {
            case CONTENT_POSTING ->
                    "This feature allows users to post content on the news Feed. Users can post text and Images";
            case CONTENT_FEED ->
                    "This feature allows users to view content. Users can view posts from other users and comment on them. Users can also like posts.";
            case PRIVATE_MESSAGING -> "This feature allows users to send private messages to other users";
            case GROUP ->
                    "This feature allows users to create groups, join, and have discussions on the group page. Moderators can also create events in the group page.";
            case MODULE ->
                    "This feature allows Module Leaders to manage interactions with students. Module Leaders can post announcements, create events.";
            case EVENTS ->
                    "This feature allows users to create events. Users can view events created by groups and module";
            case POST_COMMENTING ->
                    "This feature allows users to comment on posts. Disabling this feature will prevent users from commenting on posts";
            default -> "This feature is not available";
        };
    }


}
