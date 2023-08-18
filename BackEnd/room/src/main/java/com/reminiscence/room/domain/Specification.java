package com.reminiscence.room.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Specification {
    HORIZONTAL("horizontal"),
    VERTICAL("vertical");

    private final String value;

    Specification(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Specification from(String value) {
        for (Specification type : Specification.values()) {
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
