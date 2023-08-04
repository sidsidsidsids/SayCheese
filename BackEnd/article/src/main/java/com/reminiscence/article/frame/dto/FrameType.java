package com.reminiscence.article.frame.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FrameType {
    IMAGE("image"),
    FRAME("frame"),
    PROFILE("profile");

    private final String value;

    private FrameType(String value) {
        this.value = value;
    }
    @JsonCreator
    public static FrameType from(String value) {
        for (FrameType type : FrameType.values()) {
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
