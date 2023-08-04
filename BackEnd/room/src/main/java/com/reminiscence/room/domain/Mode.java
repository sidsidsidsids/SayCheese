package com.reminiscence.room.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Mode {
    GAME("game"),
    NORMAL("normal");

    private final String value;

    Mode(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Mode from(String value) {
        for (Mode type : Mode.values()) {
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
