package com.reminiscense.batch.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FrameSpecification {
    VERTICAL("vertical"),
    HORIZONTAL("horizontal");


    private final String value;

    FrameSpecification(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FrameSpecification from(String value) {
        for (FrameSpecification type : FrameSpecification.values()) {
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
