package com.reminiscense.batch.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FileType {
    IMAGE("image"),
    FRAME("frame"),
    PROFILE("profile");

    private final String value;

    private FileType(String value) {
        this.value = value;
    }
    @JsonCreator
    public static FileType from(String value) {
        for (FileType type : FileType.values()) {
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
