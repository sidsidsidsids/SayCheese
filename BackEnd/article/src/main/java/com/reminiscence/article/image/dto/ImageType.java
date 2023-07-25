package com.reminiscence.article.image.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ImageType {
    IMAGE("image"),
    FRAME("frame"),
    PROFILE("profile");

    private final String value;

    ImageType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ImageType from(String value) {
        for (ImageType type : ImageType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
